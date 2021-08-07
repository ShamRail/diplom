package ru.ugasu.app.service;

import ru.ugasu.app.model.Project;

import java.util.Optional;

public interface ProjectService {
    Optional<Project> findById(int projectID);
    Project save(Project project);
}
