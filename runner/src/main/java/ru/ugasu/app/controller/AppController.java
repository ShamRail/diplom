package ru.ugasu.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.ugasu.app.model.Project;
import ru.ugasu.app.model.run.App;
import ru.ugasu.app.repo.AppRepository;
import ru.ugasu.app.service.AppService;
import ru.ugasu.app.service.AppStatus;
import ru.ugasu.app.service.ProjectService;
import ru.ugasu.app.service.command.CommandInfo;
import ru.ugasu.app.service.command.CommandStatus;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@Controller
@RequestMapping
public class AppController {

    @Autowired
    private AppService appService;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/run")
    public @ResponseBody App runApp(@RequestParam("projectID") int projectID) {
        Optional<Project> optionalProject = projectService.findById(projectID);
        if (optionalProject.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid project id");
        }
        Project project = optionalProject.get();
        if (project.getBuildStatus() == null || !project.getBuildStatus().equals("BUILT") || project.getImageID() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project is not built or in building stage");
        }
        projectService.save(project);
        return appService.start(project);
    }

    @PostMapping(
            value = "/uploadFile",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public @ResponseBody CommandInfo uploadFile(
            @RequestParam("appID") int appID,
            @RequestParam("filePath") String filePath,
            @RequestParam("file") MultipartFile file) {
        App app = validateAppRequest(appID);
        try {
            CommandInfo commandInfo = appService.copyIn(app, file.getInputStream(), Path.of(filePath));
            if (commandInfo.getCommandStatus() != CommandStatus.COMPLETE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, commandInfo.getMessage());
            }
            return commandInfo;
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Failed during copying file. %s occurs. %s", e.getClass(), e.getMessage())
            );
        }
    }

    @GetMapping(value = "/downloadFile",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE}
    )
    public @ResponseBody byte[] downloadFile(@RequestParam("appID") int appID, @RequestParam("filePath") String filePath) {
        App app = validateAppRequest(appID);
        CommandInfo commandInfo = appService.copyFrom(app, Path.of(filePath));
        if (commandInfo.getCommandStatus() != CommandStatus.COMPLETE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, commandInfo.getMessage());
        }
        try (FileInputStream input = new FileInputStream(Path.of(app.getAppPath(), filePath).toFile())) {
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            input.transferTo(content);
            return content.toByteArray();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Failed during copying file. %s occurs. %s", e.getClass(), e.getMessage())
            );
        }
    }

    @GetMapping(value = "/kill", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> kill(@RequestParam("id") int id) {
        App app = validateAppRequest(id);
        if (app.getAppStatus() == AppStatus.DIED) {
            return ResponseEntity.ok().build();
        }
        CommandInfo commandInfo = appService.kill(app);
        if (commandInfo.getCommandStatus() != CommandStatus.COMPLETE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, commandInfo.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    private App validateAppRequest(int appID) {
        Optional<App> appOptional = appRepository.findById(appID);
        if (appOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "App id is not correct");
        }
        App app = appOptional.get();
        if (app.getAppStatus() != AppStatus.STARTED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "App is not STARTED state");
        }
        return app;
    }

}
