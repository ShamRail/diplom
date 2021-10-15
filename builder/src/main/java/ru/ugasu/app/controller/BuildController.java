package ru.ugasu.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ugasu.app.model.build.Build;
import ru.ugasu.app.model.build.BuildStatus;
import ru.ugasu.app.model.build.Project;
import ru.ugasu.app.model.dto.ProjectDTO;
import ru.ugasu.app.repo.BuildRepository;
import ru.ugasu.app.repo.ProjectRepository;
import ru.ugasu.app.service.build.BuildService;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * API для сборки проект
 * /build. Команда для сборки проект
 * /build-status?projectID. Команда для проверки статуса сборки
 */
@RestController
public class BuildController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BuildService buildService;

    @Autowired
    private BuildRepository buildRepository;

    @PostMapping("/build")
    public Build buildProject(@RequestBody ProjectDTO projectDTO) {

        fieldValidation(projectDTO);

        Optional<Build> buildOptional = buildService.getBuild(new Project(projectDTO.getId()));
        if (buildOptional.isPresent()) {
            BuildStatus buildStatus = buildOptional.get().getBuildStatus();
            if (buildStatus != BuildStatus.BUILT && buildStatus != BuildStatus.FALLEN) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Build is already running!");
            }
        }

        return buildService.start(projectRepository.save(mapDTO(projectDTO)));
    }

    @GetMapping("/build-status")
    public Build getBuildStatus(@RequestParam("projectID") int projectID) {
        Optional<Build> buildOptional = buildService.getBuild(new Project(projectID));
        if (buildOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Build is not running yet!");
        }
        return buildOptional.get();
    }

    @DeleteMapping("/deleteProject")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(@RequestParam("projectID") int projectID) {
        Optional<Project> optionalProject = projectRepository.findById(projectID);
        if (optionalProject.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project is not found!");
        }
        Optional<Build> buildOptional = buildService.getBuild(new Project(projectID));
        if (buildOptional.isPresent() && buildOptional.get().getBuildStatus() == BuildStatus.BUILDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project is building now. Removing will be available after buildings endings.");
        }
        buildService.removeProject(optionalProject.get());
    }

    private Project mapDTO(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setArchiveInnerDir(projectDTO.getArchiveInnerDir());
        project.setBuildCommand(projectDTO.getBuildCommand());
        project.setConfigurationId(projectDTO.getConfigID());
        project.setInFiles(projectDTO.getInFiles());
        project.setOutFiles(projectDTO.getOutFiles());
        project.setRunCommand(projectDTO.getRunCommand());
        project.setSourceCodeURL(projectDTO.getSourceCodeURL());
        project.setName(projectDTO.getName());
        return project;
    }

    private void fieldValidation(ProjectDTO projectDTO) {
        if (projectDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is not specified");
        }
        if (projectDTO.getSourceCodeURL() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source code URL is not specified");
        }
        if (projectDTO.getBuildCommand() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Build command is not specified");
        }
        if (projectDTO.getRunCommand() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Running command is not specified");
        }
        if (projectDTO.getConfigID() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Config is not specified");
        }
    }

}
