package ru.ugasu.app.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ugasu.app.model.build.BuildStatus;
import ru.ugasu.app.model.build.Project;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test
    public void whenCreate() {
        Project project = createProject(1);
        Project output = projectRepository.findById(project.getId()).orElse(null);
        assertEquals(project, output);
    }

    @Test
    public void whenUpdate() {
        Project project = createProject(1);

        Project updated = new Project(
                project.getId(), "upd my app", "http://github.com/upd",
                "upd mvn install", "upd java -jar target/program.jar", 2
        );
        updated.setBuildStatus(BuildStatus.BUILDING);
        updated.setImageID("1245abcde");
        projectRepository.save(updated);

        Project output = projectRepository.findById(updated.getId()).orElse(null);

        assertEquals(1, projectRepository.findAll().size());
        isEquals(updated, output);
    }

    @Test
    public void whenDelete() {
        Project project = createProject(1);
        projectRepository.deleteById(project.getId());
        assertTrue(projectRepository.findById(project.getId()).isEmpty());
    }

    @Test
    public void whenFindAll() {
        Project project = createProject(1);

        Project updated = new Project(
                2, "upd my app", "http://github.com/upd",
                "upd mvn install", "upd java -jar target/program.jar", 2
        );
        updated.setBuildStatus(BuildStatus.BUILDING);
        updated.setImageID("1245abcde");
        projectRepository.save(updated);

        List<Project> projects = projectRepository.findAll();

        assertEquals(2, projects.size());
        isEquals(project, projects.get(0));
        isEquals(updated, projects.get(1));
    }

    @Test
    public void whenUpdateStatus() {
        Project project = createProject(1);
        projectRepository.updateStatusById(project.getId(), BuildStatus.BUILDING);
        assertEquals(BuildStatus.BUILDING, projectRepository.findById(project.getId()).get().getBuildStatus());
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
