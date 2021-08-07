package ru.ugasu.app.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ugasu.app.model.build.Build;
import ru.ugasu.app.model.build.BuildStatus;
import ru.ugasu.app.model.build.Project;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BuildRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    BuildRepository buildRepository;

    @Test
    public void whenCreate() {
        Project project = createProject(1);
        Build build = createBuild(project);
        Build output = buildRepository.getOne(build.getId());
        assertEquals(build, output);
        isEquals(project, build.getProject());
        assertEquals(build.getBuildStatus(), output.getBuildStatus());
    }

    @Test
    public void whenDelete() {
        Project project = createProject(1);
        Build build = createBuild(project);
        buildRepository.deleteById(build.getId());
        assertFalse(buildRepository.existsById(build.getId()));
    }

    @Test
    public void whenUpdate() {
        Project project = createProject(1);
        Build build = createBuild(project);
        build.setBuildStatus(BuildStatus.STARTED);
        buildRepository.save(build);
        assertEquals(BuildStatus.STARTED, buildRepository.getOne(build.getId()).getBuildStatus());
    }

    @Test
    public void whenUpdateStatusById() {
        Project project = createProject(1);
        Build build = createBuild(project);
        buildRepository.updateStatusById(build.getId(), BuildStatus.STARTED);
        assertEquals(BuildStatus.STARTED, buildRepository.getOne(build.getId()).getBuildStatus());
    }

    public Build createBuild(Project project) {
        Build build = new Build(project, "msg");
        build.setStartAt(LocalDateTime.now().minusMinutes(5));
        build.setEndAt(LocalDateTime.now());
        build.setBuildStatus(BuildStatus.BUILT);
        return buildRepository.save(build);
    }

    public Project createProject(Integer configurationId) {
        Project project = new Project(
                1, "my app", "http://github.com",
                "mvn install", "java -jar target/program.jar", configurationId
        );
        project.setBuildStatus(BuildStatus.STARTED);
        project.setImageID("123abc");
        return projectRepository.save(project);
    }

    public void isEquals(Project project, Project output) {
        assertEquals(project.getConfigurationId(), output.getConfigurationId());
        assertEquals(project, output);
        assertEquals(project.getName(), output.getName());
        assertEquals(project.getBuildCommand(), output.getBuildCommand());
        assertEquals(project.getImageID(), output.getImageID());
        assertEquals(project.getBuildStatus(), output.getBuildStatus());
        assertEquals(project.getInFiles(), output.getInFiles());
        assertEquals(project.getOutFiles(), output.getOutFiles());
        assertEquals(project.getSourceCodeURL(), output.getSourceCodeURL());
    }

}
