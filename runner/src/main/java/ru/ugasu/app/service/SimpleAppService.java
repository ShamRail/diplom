package ru.ugasu.app.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ugasu.app.model.BaseEntity;
import ru.ugasu.app.model.Project;
import ru.ugasu.app.model.run.App;
import ru.ugasu.app.repo.AppRepository;
import ru.ugasu.app.service.command.CommandInfo;
import ru.ugasu.app.service.command.CommandStatus;
import ru.ugasu.app.service.io.DecompressService;
import ru.ugasu.app.service.logger.AppLogger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * Сервис для работы с контейнером приложения
 */
@Service
@EnableScheduling
// @EnableAspectJAutoProxy(proxyTargetClass = true)
public class SimpleAppService implements AppService {

    @Value("${app.apps}")
    private String appsPath;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAppService.class.getSimpleName());

    public static final int REMOVING_DELAY = 1; // in hours

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private DockerClient dockerClient;

    private static final int CONTAINER_WEBSOCKET_PORT = 80;

    @Autowired
    @Qualifier("tarDecompressService")
    private DecompressService decompressService;

    @Value("${DOCKER_NET}")
    private String netWork;

    @Value("${WEBSOCKET_HOST}")
    private String websocketHost;

    @PostConstruct
    private void initDir() {
        try {
            Files.createDirectories(Paths.get(appsPath));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Создает и запускает контейнер приложения.
     * При создании контейнера, также создает локальная директория, куда предварительно скачиваются файлы. С этой
     * директории также будет идти раздача файлов.
     *
     * @param project проект, на основе образа, которого будет создаваться контейнер. Проект обязательно
     *                должен быть загружен из БД и быть в контексте, т.к. вызываются методы project
     * @return app приложение с сообщением о результатах запуска и статусе.
     */
    @Override
    public App start(Project project) {
        if (project.getBuildStatus() == null || !project.getBuildStatus().equals("BUILT") || project.getImageID() == null) {
            App app = new App();
            app.setMessage("Failed to start up. Project is not built");
            app.setAppStatus(AppStatus.FALLEN);
            return app;
        }

        String appPath = Path.of(appsPath, UUID.randomUUID().toString() + System.currentTimeMillis()).toString();
        App app = new App(project, appPath);
        app.setStartAt(LocalDateTime.now());
        app.setAppPath(appPath);
        app = appRepository.save(app);

        try {
            Files.createDirectory(Path.of(app.getAppPath()));

            ExposedPort exposedPort = ExposedPort.tcp(CONTAINER_WEBSOCKET_PORT);
            // Ports ports = new Ports();
            // ports.bind(exposedPort, Ports.Binding.bindPort(8090));

            String containerName = "app" + System.currentTimeMillis();
            String containerId = dockerClient.createContainerCmd(project.getImageID())
                    .withTty(true).withStdinOpen(true)
                    .withExposedPorts(exposedPort)
                    //.withHostConfig(HostConfig.newHostConfig().withPortBindings(ports))
                    .withName(containerName)
                    .exec().getId();
            dockerClient.startContainerCmd(containerId).exec();

            dockerClient.connectToNetworkCmd()
                    .withContainerId(containerId)
                    .withNetworkId(netWork).exec();

            String message = "Environment is started";
            // app.setWsURI(websocketHost);
            app.setWsURI(String.format("ws://%s/ws/%s", websocketHost, containerName));
            app.setContainerID(containerId);

            updateInDb(app, AppStatus.STARTED, message);
        } catch (Exception e) {
            String message = String.format(
                    "Failed to create and start container. %s occurs. Exception message: %s",
                    e.getClass(), e.getMessage()
            );
            updateInDb(app, AppStatus.FALLEN, message);
        }
        return app;
    }

    /**
     * Копирует содержимое в файл. Важно, что file это путь конечного файла, а не директории.
     * Конечнй файл должен быть как минимум в одной директории, т.к. апи поддерживает только копирование диреторий.
     * Сначала создаются директории и файл в локально. Далее переносится директория в которой хранятся файлы
     *
     * @param app     контейнер, в который копируем. App должен быть в контексте, т.е. загружен из БД
     * @param content содержимое которое копируемся
     * @param file    путь в контейнере
     * @return commandInfo, содержащий результаты копирования в виде сообщения и статуса.
     */
    @Override
    public CommandInfo copyIn(App app, InputStream content, Path file) {
        CommandInfo commandInfo = new CommandInfo();
        if (app.getAppStatus() != AppStatus.STARTED) {
            commandInfo.setMessage("Failed to copy in. App is not started!");
            commandInfo.setCommandStatus(CommandStatus.FALLEN);
            return commandInfo;
        }
        try {

            Path absoluteFilePath = Path.of(app.getAppPath(), file.getParent().toString());
            Files.createDirectories(absoluteFilePath);

            Path fileFullPath = Path.of(absoluteFilePath.toString(), file.getFileName().toString());
            if (Files.exists(fileFullPath)) {
                Files.delete(fileFullPath);
            }

            Files.createFile(fileFullPath);
            try (OutputStream out = new FileOutputStream(fileFullPath.toFile())) {
                content.transferTo(out);
            }

            dockerClient.copyArchiveToContainerCmd(app.getContainerID())
                    .withHostResource(up(Path.of(app.getAppPath()), absoluteFilePath).toString())
                    .withRemotePath("/app").exec();

            commandInfo.setCommandStatus(CommandStatus.COMPLETE);
            commandInfo.setMessage("File successfully copied!");

        } catch (Exception e) {
            commandInfo.setMessage(String.format(
                    "Failed during file copying. %s occurs. Message: %s", e.getClass(), e.getMessage()
            ));
            commandInfo.setCommandStatus(CommandStatus.FALLEN);
        }
        return commandInfo;
    }

    /**
     * Копирует файлы из контейнера. Сначала создается такая же иерархия директорий. Далее переносится директория
     * Если результат команды COMPLETE. Значит по пути file из контекста локальной директории приложения можно получить файл
     *
     * @param app  контейнер, в который копируем. App должен быть в контексте, т.е. загружен из БД
     * @param file путь в контейнере в конечному файлу
     * @return commandInfo. Результат выполнения команды в виде сообщения и статуса
     */
    @Override
    public CommandInfo copyFrom(App app, Path file) {
        CommandInfo commandInfo = new CommandInfo();
        if (app.getAppStatus() != AppStatus.STARTED) {
            String message = "Failed to copy out. App is started!";
            commandInfo.setMessage(message);
            commandInfo.setCommandStatus(CommandStatus.FALLEN);
            return commandInfo;
        }
        try {

            Path absolutePath = Path.of(app.getAppPath(), file.getParent().toString());
            Files.createDirectories(absolutePath);

            Path fileFullPath = Path.of(absolutePath.toString(), file.getFileName().toString());
            if (Files.exists(fileFullPath)) {
                Files.delete(fileFullPath);
            }

            try (InputStream in = dockerClient.copyArchiveFromContainerCmd(app.getContainerID(),
                    "/app/" + file.getParent().toString()).exec()) {
                decompressService.decompress(in, absolutePath.getParent());
                String message = "Copying is complete";
                commandInfo.setCommandStatus(CommandStatus.COMPLETE);
                commandInfo.setMessage(message);
            }
        } catch (Exception e) {
            commandInfo.setMessage(String.format(
                    "Failed to copy out. %s occurs. Message: %s", e.getClass(), e.getMessage())
            );
            commandInfo.setCommandStatus(CommandStatus.FALLEN);
        }
        return commandInfo;
    }

    /**
     * Уничтожает контейнер в котором шла работы с приложением
     *
     * @param app приложение, которое находится в контейнере. App должен быть в контексте, т.е. загружен из БД
     * @return commandInfo. Результат выполнения команды в виде сообщения и статуса
     */
    @Override
    public CommandInfo kill(App app) {
        app.setEndAt(LocalDateTime.now());
        String message = "Container is removed";
        CommandInfo commandInfo = new CommandInfo(message, CommandStatus.COMPLETE);
        try {
            dockerClient.removeContainerCmd(app.getContainerID()).withForce(true).exec();
        } catch (Exception e) {
            message = String.format("Removing is failed. %s occurs. Message: %s", e.getClass(), e.getMessage());
            commandInfo.setMessage(message);
            commandInfo.setCommandStatus(CommandStatus.FALLEN);
        }
        updateInDb(app, AppStatus.DIED, message);
        return commandInfo;
    }

    @Scheduled(fixedDelay = 15 * 60 * 1000)
    @Async
    public void removeOldContainers() {
        LOGGER.info("Run removing old container task.");
        var workingApps = appRepository.findAll().stream()
                .filter(app -> app.getContainerID() != null)
                .filter(app -> app.getAppStatus() != null && app.getAppStatus() != AppStatus.DIED)
                .collect(Collectors.toMap(App::getContainerID, app -> app));
        var containersIds = dockerClient.listContainersCmd().withShowAll(true).exec().stream()
                .filter(container -> Arrays.stream(container.getNames()).anyMatch(n -> n.startsWith("/app")))
                .map(Container::getId)
                .collect(Collectors.toSet());
        for (Map.Entry<String, App> entry : workingApps.entrySet()) {
            var app = entry.getValue();
            var creatingTime = app.getStartAt();
            var limit = LocalDateTime.now().minusHours(REMOVING_DELAY);
            if (!containersIds.contains(entry.getKey()) || creatingTime.isBefore(limit)) {
                LOGGER.info("Remove old container with id {}", app.getContainerID());
                LOGGER.info("App exists on db. Killing from AppService");
                kill(app);
            }
        }
        for (String id : containersIds) {
            if (!workingApps.containsKey(id)) {
                try {
                    LOGGER.info("App doesnt exist on db. Killing by Docker engine directly");
                    dockerClient.removeContainerCmd(id).withForce(true).exec();
                } catch (Exception e) {
                    LOGGER.info("Failed to remove container. {}. {}", e.getMessage(), e.getMessage());
                }
            }
        }
    }

    private void updateInDb(App app, AppStatus appStatus, String message) {
        app.setAppStatus(appStatus);
        app.setMessage(message);
        appRepository.save(app);
    }

    private Path up(Path appPath, Path current) {
        if (appPath.equals(current.getParent())) {
            return current;
        }
        return up(appPath, current.getParent());
    }

}
