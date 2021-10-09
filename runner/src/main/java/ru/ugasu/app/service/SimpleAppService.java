package ru.ugasu.app.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ExposedPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Сервис для работы с контейнером приложений
 */
@Service
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
public abstract class SimpleAppService implements AppService {

    @Value("${app.apps}")
    private String appsPath;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private DockerClient dockerClient;

    private final Map<Integer, AppLogger> loggers = new ConcurrentHashMap<>();

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

    @PreDestroy
    private void flushLog() {
        loggers.values().forEach(AppLogger::flush);
    }

    /**
     * Асихронно записываем каждые 10 секунд лог запущенных приложений
     */
    @Scheduled(fixedDelay = 10_000)
    @Async
    void flush() {
        loggers.values().forEach(AppLogger::flush);
    }

    /**
     * Создает и запускает контейнер приложения.
     * При создании контейнера, также создает локальная директория, куда предварительно скачиваются файлы. С этой
     * директории также будет идти раздача файлов.
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
        loggers.put(app.getId(), appLogger(Path.of(appPath, "log.txt").toString()));

        try {
            logOut(app, "Create app root directory");
            Files.createDirectory(Path.of(app.getAppPath()));

            logOut(app, "Create container and start it");
            String containerName = "app" + System.currentTimeMillis();
            String containerId = dockerClient.createContainerCmd(project.getImageID())
                    .withTty(true).withStdinOpen(true)
                    .withExposedPorts(ExposedPort.tcp(80))
                    .withName(containerName)
                    .exec().getId();
            dockerClient.startContainerCmd(containerId).exec();

            logOut(app, "Connect container to network");
            dockerClient.connectToNetworkCmd()
                    .withContainerId(containerId)
                    .withNetworkId(netWork).exec();

            String message = "Environment is started";
            app.setWsURI(String.format("%s/ws/%s", websocketHost, containerName));
            app.setContainerID(containerId);

            updateInDb(app, AppStatus.STARTED, message);
            logOut(app, message);
        } catch (Exception e) {
            String message = String.format(
                    "Failed to create and start container. %s occurs. Exception message: %s",
                    e.getClass(), e.getMessage()
            );
            updateInDb(app, AppStatus.FALLEN, message);
            logOut(app, message);
        }
        return app;
    }

    /**
     * Копирует содержимое в файл. Важно, что file это путь конечного файла, а не директории.
     * Конечнй файл должен быть как минимум в одной директории, т.к. апи поддерживает только копирование диреторий.
     * Сначала создаются директории и файл в локально. Далее переносится директория в которой хранятся файлы
     * @param app контейнер, в который копируем. App должен быть в контексте, т.е. загружен из БД
     * @param content содержимое которое копируемся
     * @param file путь в контейнере
     * @return commandInfo, содержащий результаты копирования в виде сообщения и статуса.
     */
    @Override
    public CommandInfo copyIn(App app, InputStream content, Path file) {
        CommandInfo commandInfo = new CommandInfo();
        logOut(app, "Try to copy in");
        if (app.getAppStatus() != AppStatus.STARTED) {
            String message = "Failed to copy in. App is not started!";
            logOut(app, message);
            commandInfo.setMessage(message);
            commandInfo.setCommandStatus(CommandStatus.FALLEN);
        }
        try {

            logOut(app, "Create subdirs of specified apps path");
            Path absoluteFilePath = Path.of(app.getAppPath(), file.getParent().toString());
            Files.createDirectories(absoluteFilePath);

            logOut(app, "Create file and write data");
            Path fileFullPath = Path.of(absoluteFilePath.toString(), file.getFileName().toString());
            if (Files.exists(fileFullPath)) {
                logOut(app, "File is already exists. Remove it");
                Files.delete(fileFullPath);
            }

            Files.createFile(fileFullPath);
            try (OutputStream out = new FileOutputStream(fileFullPath.toFile())) {
                content.transferTo(out);
            }

            logOut(app, "Writing complete. Try to copy file from host");
            dockerClient.copyArchiveToContainerCmd(app.getContainerID())
                    .withHostResource(up(Path.of(app.getAppPath()), absoluteFilePath).toString())
                    .withRemotePath("/app").exec();

            commandInfo.setCommandStatus(CommandStatus.COMPLETE);
            String message = "File successfully copied!";
            commandInfo.setMessage(message);
            logOut(app, message);

        } catch (Exception e) {
            String message = String.format(
                    "Failed during file copying. %s occurs. Message: %s", e.getClass(), e.getMessage()
            );
            logOut(app, message);
            commandInfo.setMessage(message);
            commandInfo.setCommandStatus(CommandStatus.FALLEN);
        }
        return commandInfo;
    }

    /**
     * Копирует файлы из контейнера. Сначала создается такая же иерархия директорий. Далее переносится директория
     * Если результат команды COMPLETE. Значит по пути file из контекста локальной директории приложения можно получить файл
     * @param app контейнер, в который копируем. App должен быть в контексте, т.е. загружен из БД
     * @param file путь в контейнере в конечному файлу
     * @return commandInfo. Результат выполнения команды в виде сообщения и статуса
     */
    @Override
    public CommandInfo copyFrom(App app, Path file) {
        CommandInfo commandInfo = new CommandInfo();
        logOut(app, "Try to copy out");
        if (app.getAppStatus() != AppStatus.STARTED) {
            String message = "Failed to copy out. App is started!";
            logOut(app, message);
            commandInfo.setMessage(message);
            commandInfo.setCommandStatus(CommandStatus.FALLEN);
        }

        try {

            logOut(app, "Create subdirs of file");
            Path absolutePath = Path.of(app.getAppPath(), file.getParent().toString());
            Files.createDirectories(absolutePath);

            Path fileFullPath = Path.of(absolutePath.toString(), file.getFileName().toString());
            if (Files.exists(fileFullPath)) {
                logOut(app, "File is already exists. Remove it");
                Files.delete(fileFullPath);
            }

            logOut(app, "Copy from file from remote app");
            try (InputStream in = dockerClient.copyArchiveFromContainerCmd(app.getContainerID(),
                    "/app/" + file.getParent().toString()).exec()) {
                logOut(app, "Decompressing tar archive");
                decompressService.decompress(in, absolutePath.getParent());
                String message = "Copying is complete";
                logOut(app, message);
                commandInfo.setCommandStatus(CommandStatus.COMPLETE);
                commandInfo.setMessage(message);
            }
        } catch (Exception e) {
            String message = String.format(
                    "Failed to copy out. %s occurs. Message: %s",
                    e.getClass(), e.getMessage());
            logOut(app, message);
            commandInfo.setMessage(message);
            commandInfo.setCommandStatus(CommandStatus.FALLEN);
        }

        return commandInfo;
    }

    /**
     * Уничтожает контейнер в котором шла работы с приложением
     * @param app приложение, которое находится в контейнере. App должен быть в контексте, т.е. загружен из БД
     * @return commandInfo. Результат выполнения команды в виде сообщения и статуса
     */
    @Override
    public CommandInfo kill(App app) {
        logOut(app, "Try to remove container");
        app.setEndAt(LocalDateTime.now());
        CommandInfo commandInfo = new CommandInfo();
        try {
            dockerClient.removeContainerCmd(app.getContainerID())
                    .withForce(true)
                    .exec();
            String message = "Container is removed";
            commandInfo.setMessage(message);
            commandInfo.setCommandStatus(CommandStatus.COMPLETE);
            updateInDb(app, AppStatus.DIED, message);
            AppLogger appLogger = loggers.remove(app.getId());
            if (appLogger != null) {
                appLogger.flush();
            }
        } catch (Exception e) {
            String message = String.format(
                    "Removing is failed. %s occurs. Message: %s",
                    e.getClass(), e.getMessage()
            );
            commandInfo.setMessage(message);
            commandInfo.setCommandStatus(CommandStatus.FALLEN);
            updateInDb(app, AppStatus.FALLEN, message);
            logOut(app, message);
        }
        return commandInfo;
    }

    private void logOut(App app, String message) {
        AppLogger appLogger = loggers.get(app.getId());
        if (appLogger != null) {
            appLogger.append(message);
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

    @Lookup
    protected abstract AppLogger appLogger(String path);

}
