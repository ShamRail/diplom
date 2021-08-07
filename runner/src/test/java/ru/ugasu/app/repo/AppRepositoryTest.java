package ru.ugasu.app.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ugasu.app.model.Project;
import ru.ugasu.app.model.run.App;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AppRepository appRepository;

    @Test
    public void whenCreate() {
        Project project = createProject();
        App app = createApp(project);
        App output = appRepository.getOne(app.getId());
        isEquals(app, output);
        assertEquals(app, output);
    }

    @Test
    public void whenDelete() {
        Project project = createProject();
        App app = createApp(project);
        appRepository.deleteById(app.getId());
        assertFalse(appRepository.existsById(app.getId()));
    }

    @Test
    public void whenUpdate() {
        Project project = createProject();
        App app = createApp(project);
        app.setStartAt(LocalDateTime.now().minusMinutes(10));
        app.setEndAt(LocalDateTime.now().plusMinutes(2));
        app.setWsURI("upd/container/ws/123");
        appRepository.save(app);
        isEquals(app, appRepository.getOne(app.getId()));
    }

    @Test
    public void whenFindAll() {
        Project project = createProject();
        App app1 = createApp(project);
        List<App> apps = appRepository.findAll();
        assertEquals(1, apps.size());
        isEquals(app1, apps.get(0));
    }

    public Project createProject() {
        Project project = new Project(
                12345, "java+maven", "java -jar program.jar", "in/phrases.txt",
                "out/phrases.txt", "123", "BUILT", 1
        );
        return projectRepository.save(project);
    }

    public App createApp(Project project) {
        App app = new App(project, "path/to/log");
        app.setStartAt(LocalDateTime.now().minusMinutes(5));
        app.setEndAt(LocalDateTime.now());
        app.setWsURI("/container/ws/123");
        return appRepository.save(app);
    }

    public void isEquals(App app, App output) {
        assertEquals(output.getProject(), app.getProject());
        assertEquals(output.getAppPath(), app.getAppPath());
        assertEquals(output.getContainerID(), app.getContainerID());
        assertEquals(output.getProject().getRunCommand(), app.getProject().getRunCommand());
        assertEquals(output.getStartAt(), app.getStartAt());
        assertEquals(output.getEndAt(), app.getEndAt());
        assertEquals(output.getProject().getInFiles(), app.getProject().getInFiles());
        assertEquals(output.getProject().getInFiles(), app.getProject().getInFiles());
        assertEquals(output.getProject().getName(), app.getProject().getName());
        assertEquals(output.getProject().getConfigurationId(), app.getProject().getConfigurationId());
        assertEquals(output.getWsURI(), app.getWsURI());
    }

}
