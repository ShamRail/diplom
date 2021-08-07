package ru.ugasu.app.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Проект, из которого происходит сборка образа.
 * На основе образа создается контейнер.
 * Отдельно взятый пользователь работает с отдельно взятым контейнером
 */
@Entity
@Table(name = "project")
public class Project {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    /**
     * Команда для запуска проекта
     */
    @Column(name = "run_command")
    private String runCommand;

    /**
     * Хранит пути к файлам, который будут загружены через ;
     * Например, in/file.txt;in/file.json
     * Если это поле не задано, то входных данных в виде файлов программа не имеет
     */
    @Column(name = "in_files")
    private String inFiles;

    /**
     * Хранит пути к файлам, который будут выгружены через ;
     * Например, out/file.txt;out/file.json
     * Если это поле не задано, то выходных данных в виде файлов программа не имеет
     */
    @Column(name = "out_files")
    private String outFiles;

    /**
     * id собранного образа. При успешной сборке проекта формируется образ.
     */
    @Column(name = "image_id")
    private String imageID;

    @Column(name = "build_status")
    private String buildStatus;

    @Column(name = "configuration_id")
    private Integer configurationId;

    public Project() { }

    public Project(Integer id) {
        this.id = id;
    }

    public Project(Integer id, String name,
                   String runCommand,
                   String inFiles, String outFiles,
                   String imageID, String buildStatus, Integer configurationId) {
        this.id = id;
        this.name = name;
        this.runCommand = runCommand;
        this.inFiles = inFiles;
        this.outFiles = outFiles;
        this.imageID = imageID;
        this.buildStatus = buildStatus;
        this.configurationId = configurationId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRunCommand() {
        return runCommand;
    }

    public void setRunCommand(String runCommand) {
        this.runCommand = runCommand;
    }

    public String getInFiles() {
        return inFiles;
    }

    public void setInFiles(String inFiles) {
        this.inFiles = inFiles;
    }

    public String getOutFiles() {
        return outFiles;
    }

    public void setOutFiles(String outFiles) {
        this.outFiles = outFiles;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Integer configurationId) {
        this.configurationId = configurationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        return id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
