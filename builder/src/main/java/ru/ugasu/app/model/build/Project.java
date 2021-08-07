package ru.ugasu.app.model.build;

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

    /**
     * Название проекта
     */
    @Column(name = "name")
    private String name;

    /**
     * Ссылка на исходный код. Код должен быть в архиве
     */
    @Column(name = "source_code_url")
    private String sourceCodeURL;

    /**
     * Команда для сборки проекта
     */
    @Column(name = "build_command")
    private String buildCommand;

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
     * Конфигурация на основе, которой происходит сборка проекта
     */

    @Column(name = "configuration_id")
    private Integer configurationId;

    /**
     * Состояние сборки. Запуск возможен только для собранного проекта
     */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "build_status")
    private BuildStatus buildStatus;

    /**
     * id собранного образа. При успешной сборке проекта формируется образ.
     */
    @Column(name = "image_id")
    private String imageID;

    /**
     * Директория внутри архива
     */
    @Column(name = "archive_inner_dir")
    private String archiveInnerDir;

    public Project() { }

    public Project(Integer id) {
        this.id = id;
    }

    public Project(String name, String sourceCodeURL,
                   String buildCommand, String runCommand, Integer configurationId) {
        this.name = name;
        this.sourceCodeURL = sourceCodeURL;
        this.buildCommand = buildCommand;
        this.runCommand = runCommand;
        this.configurationId = configurationId;
    }

    public Project(int id, String name, String sourceCodeURL,
                   String buildCommand, String runCommand, Integer configurationId) {
        this.id = id;
        this.name = name;
        this.sourceCodeURL = sourceCodeURL;
        this.buildCommand = buildCommand;
        this.runCommand = runCommand;
        this.configurationId = configurationId;
    }

    public Project(int id, String name, String sourceCodeURL, String archiveInnerDir,
                   String buildCommand, String runCommand, Integer configurationId) {
        this.id = id;
        this.name = name;
        this.sourceCodeURL = sourceCodeURL;
        this.buildCommand = buildCommand;
        this.runCommand = runCommand;
        this.configurationId = configurationId;
        this.archiveInnerDir = archiveInnerDir;
    }

    public String getArchiveInnerDir() {
        return archiveInnerDir;
    }

    public void setArchiveInnerDir(String archiveInnerDir) {
        this.archiveInnerDir = archiveInnerDir;
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

    public String getRunCommand() {
        return runCommand;
    }

    public void setRunCommand(String runCommand) {
        this.runCommand = runCommand;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceCodeURL() {
        return sourceCodeURL;
    }

    public void setSourceCodeURL(String sourceCodeURL) {
        this.sourceCodeURL = sourceCodeURL;
    }

    public String getBuildCommand() {
        return buildCommand;
    }

    public void setBuildCommand(String buildCommand) {
        this.buildCommand = buildCommand;
    }

    public Integer getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Integer configurationId) {
        this.configurationId = configurationId;
    }

    public BuildStatus getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(BuildStatus buildStatus) {
        this.buildStatus = buildStatus;
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
        return id.equals(project.id) && Objects.equals(name, project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
