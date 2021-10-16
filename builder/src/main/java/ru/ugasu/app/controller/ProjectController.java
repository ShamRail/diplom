package ru.ugasu.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ugasu.app.model.build.Build;
import ru.ugasu.app.model.build.BuildStatus;
import ru.ugasu.app.model.build.Project;
import ru.ugasu.app.repo.ProjectRepository;
import ru.ugasu.app.service.build.BuildService;

import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    private final BuildService buildService;

    public ProjectController(ProjectRepository projectRepository, BuildService buildService) {
        this.projectRepository = projectRepository;
        this.buildService = buildService;
    }

    @RequestMapping("/{id}")
    public Project getProject(@PathVariable("id") int id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid project id"));
    }

    @DeleteMapping
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

}
