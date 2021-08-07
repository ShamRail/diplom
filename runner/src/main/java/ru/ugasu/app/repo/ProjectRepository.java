package ru.ugasu.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ugasu.app.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
