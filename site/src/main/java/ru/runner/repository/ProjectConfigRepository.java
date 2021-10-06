package ru.runner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.runner.entity.ProjectConfig;

import java.util.List;

@Repository
public interface ProjectConfigRepository extends JpaRepository<ProjectConfig, Long> {

    List<ProjectConfig> getAllByLanguage_Id(long id);

    List<ProjectConfig> getAllByBuilder_Id(long id);

    ProjectConfig getProjectConfigByBuilder_IdAndAndLanguage_Id(long languageId, long builderId);
}
