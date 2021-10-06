package ru.runner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.runner.entity.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findProjectById(Long id);
    Project findProjectByName(String name);
    List<Project> findAllByUserId(Long userId);
    List<Project> findProjectsByNameContains(String name);

}
