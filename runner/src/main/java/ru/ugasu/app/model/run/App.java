package ru.ugasu.app.model.run;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.ugasu.app.model.Project;
import ru.ugasu.app.model.TimeRanged;
import ru.ugasu.app.service.AppStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Приложение. Данная сущность описывает приложение запущенное в контейнере
 */
@Entity
@Table(name = "app")
public class App extends TimeRanged {

    /**
     * Проект на основе которого создается приложение
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    /**
     * Путь к приложению. В нем храниться лог. Передаваемые и получаемые файлы
     */
    @Column(name = "log_path")
    @JsonIgnore
    private String appPath;

    /**
     * Контейнер в котором работает приложение
     */
    @Column(name = "container_id")
    private String containerID;

    /**
     * URI, по которому можно подключиться к контейнеру по веб-сокету
     */
    @Column(name = "ws_uri")
    private String wsURI;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "app_status")
    private AppStatus appStatus;

    @Column(name = "message")
    private String message;

    public App() {

    }

    public App(String containerID) {
        this.containerID = containerID;
    }

    public App(Project project, String appPath) {
        this.project = project;
        this.appPath = appPath;
    }



    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public String getWsURI() {
        return wsURI;
    }

    public void setWsURI(String wsURI) {
        this.wsURI = wsURI;
    }

    public AppStatus getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(AppStatus appStatus) {
        this.appStatus = appStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        App app = (App) o;
        return Objects.equals(project, app.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), project);
    }
}
