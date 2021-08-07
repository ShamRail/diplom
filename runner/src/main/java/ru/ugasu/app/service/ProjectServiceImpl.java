package ru.ugasu.app.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.ugasu.app.model.Project;
import ru.ugasu.app.repo.ProjectRepository;

import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Value("${BUILDER_HOST}")
    private String builderHost;

    private final ProjectRepository projectRepository;

    private final RestTemplate restTemplate;

    public ProjectServiceImpl(ProjectRepository projectRepository, RestTemplate restTemplate) {
        this.projectRepository = projectRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<Project> findById(int projectID) {
        try {
            String json = restTemplate.getForObject(this.builderHost + "/projects/" + projectID, String.class);
            JSONObject jsonObject = new JSONObject(json);

            Project project = new Project();
            project.setId(jsonObject.getInt("id"));
            project.setName(jsonObject.getString("name"));
            project.setBuildStatus(jsonObject.getString("buildStatus"));
            project.setImageID(jsonObject.getString("imageID"));
            project.setInFiles(jsonObject.getString("inFiles"));
            project.setOutFiles(jsonObject.getString("outFiles"));
            project.setRunCommand(jsonObject.getString("runCommand"));
            project.setConfigurationId(jsonObject.getInt("configurationId"));

            return Optional.of(project);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Project save(Project project) {
        return this.projectRepository.save(project);
    }

}
