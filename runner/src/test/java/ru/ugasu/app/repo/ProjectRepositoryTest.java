package ru.ugasu.app.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ugasu.app.model.Project;

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
        Project project = createProject(2);

        Project updated = new Project(
                project.getId(), "java+maven", "java -jar program.jar(1)", "in/phrases.txt(1)",
                "out/phrases.txt(1)", "123(1)", "BUILT(1)", 1
        );
        projectRepository.save(updated);

        Project output = projectRepository.findById(updated.getId()).orElse(null);
        isEquals(updated, output);
    }

    @Test
    public void whenDelete() {
        Project project = createProject(3);
        projectRepository.deleteById(project.getId());
        assertTrue(projectRepository.findById(project.getId()).isEmpty());
    }

    @Test
    public void whenFindAll() {
        Project project = createProject(4);

        Project updated = new Project(
                project.getId() * 5, "java+maven", "java -jar program.jar(1)", "in/phrases.txt(1)",
                "out/phrases.txt(1)", "123(1)", "BUILT(1)", 1
        );
        projectRepository.save(updated);

        List<Project> projects = projectRepository.findAll();

        assertEquals(2, projects.size());
        isEquals(project, projects.get(0));
        isEquals(updated, projects.get(1));
    }


    public Project createProject(int id) {
        Project project = new Project(
                id, "java+maven",
                "java -jar program.jar", "in/phrases.txt",
                "out/phrases.txt", "123", "BUILT", 1
        );
        return projectRepository.save(project);
    }

    public void isEquals(Project project, Project output) {
        assertEquals(project, output);
        assertEquals(project.getImageID(), output.getImageID());
        assertEquals(project.getBuildStatus(), output.getBuildStatus());
        assertEquals(project.getInFiles(), output.getInFiles());
        assertEquals(project.getOutFiles(), output.getOutFiles());
    }

}
