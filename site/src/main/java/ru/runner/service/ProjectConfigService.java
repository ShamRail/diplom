package ru.runner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.runner.entity.ProjectConfig;
import ru.runner.repository.ProjectConfigRepository;

import java.util.List;

@Service
public class ProjectConfigService {

    @Autowired
    LanguageAndBuilderService languageAndBuilderService;

    @Autowired
    ProjectConfigRepository projectConfigRepository;

    public ProjectConfig getConfigById(long id) {
        return projectConfigRepository.getOne(id);
    }

    public List<ProjectConfig> getAllConfigs() {
        return projectConfigRepository.findAll();
    }

    public List<ProjectConfig> getAllConfigsByBuilderId(long id) {
        return projectConfigRepository.getAllByBuilder_Id(id);
    }

    public List<ProjectConfig> getAllConfigsByLanguageId(long id) {
        return projectConfigRepository.getAllByLanguage_Id(id);
    }

    public boolean addNewConfig(ProjectConfig projectConfig) {
        long languageId = projectConfig.getLanguage().getId();
        long builderId = projectConfig.getBuilder().getId();
        ProjectConfig configFromDb = projectConfigRepository.getProjectConfigByBuilder_IdAndAndLanguage_Id(builderId, languageId);

        if (configFromDb != null) {
            return false;
        }

        projectConfigRepository.save(projectConfig);
        return true;
    }

    public boolean updateConfig(ProjectConfig projectConfig) {
        boolean isExist = projectConfigRepository.existsById(projectConfig.getId());

        if (!isExist) {
            return false;
        }

        projectConfigRepository.save(projectConfig);
        return true;
    }

    public boolean deleteConfig(Long id) {
        boolean isExist = projectConfigRepository.existsById(id);

        if (!isExist) {
            return false;
        }

        projectConfigRepository.deleteById(id);
        return true;
    }
}
