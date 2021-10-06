package ru.runner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.runner.entity.Project;
import ru.runner.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public ProjectService() {
    }

    public void addNewProject(Project project) {
        projectRepository.save(project);
    }

    public boolean updateProject(Project project) {
        boolean isExist = projectRepository.existsById(project.getId());

        if (!isExist) {
            return false;
        }

        projectRepository.save(project);
        return true;
    }

    public Project findProjectByName(String projectName) {
        return projectRepository.findProjectByName(projectName);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findProjectById(id);
    }

    public List<Project> getAllUserProjects(Long userId) {
        return projectRepository.findAllByUserId(userId);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> searchProjectsByName(String name) {
        List<Project> projectList = new ArrayList<>();
        projectList = projectRepository.findProjectsByNameContains(name);
        return projectList;
    }

    public boolean deleteProject(Long projectId) {
        boolean isProjectExist = projectRepository.existsById(projectId);

        if (isProjectExist) {
            projectRepository.deleteById(projectId);
            return true;
        }

        return false;
    }
}
