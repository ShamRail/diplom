package ru.ugasu.app.model.build;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.ugasu.app.model.TimeRanged;

import javax.persistence.*;
import java.util.Objects;

/**
 * Сущность сборки проекта. Создается когда начинается сборка проекта
 */
@Entity
@Table(name = "build")
public class Build extends TimeRanged {

    /**
     * Статус сборки. Статус обновляется в зависимости от операции выполняемой на данный момент
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "build_status")
    private BuildStatus buildStatus;

    /**
     * Собираемый проект
     */
    @OneToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    /**
     * Дополнительная информация о сборке
     */
    @Column(name = "message")
    private String message;

    @JsonIgnore
    @Column(name = "log_path")
    private String logPath;

    @Column(name = "docker_log_path")
    private String dockerLogPath;

    public Build() { }

    public Build(Project project, String message) {
        this.project = project;
        this.message = message;
    }

    public Build(Project project, String message, String logPath) {
        this.project = project;
        this.message = message;
        this.logPath = logPath;
    }

    public Build(Project project, String message, String logPath, BuildStatus buildStatus) {
        this.project = project;
        this.message = message;
        this.logPath = logPath;
        this.buildStatus = buildStatus;
    }


    public BuildStatus getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(BuildStatus buildStatus) {
        this.buildStatus = buildStatus;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getDockerLogPath() {
        return dockerLogPath;
    }

    public void setDockerLogPath(String dockerLogPath) {
        this.dockerLogPath = dockerLogPath;
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
        Build build = (Build) o;
        return buildStatus == build.buildStatus && Objects.equals(project, build.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), buildStatus, project);
    }

    @Override
    public String toString() {
        return "Build{"
                + "buildStatus="
                + buildStatus
                + ", project=" + project
                + ", startAt=" + startAt
                + ", endAt=" + endAt
                + ", id=" + id
                + '}';
    }

}
