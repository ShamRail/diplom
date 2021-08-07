package ru.ugasu.app.model.dto;

public class ProjectDTO {

    /**
     * ID собираемого проекта. Уникальность поддерживается
     * на уровне приложения, которое вызывает сборщик
     */
    private Integer id;

    /**
     * Название проекта
     */
    private String name;

    /**
     * Ссылка на исходный код. Код должен быть в архиве zip
     */
    private String sourceCodeURL;

    /**
     * Команда для сборки проекта
     */
    private String buildCommand;

    /**
     * Команда для запуска проекта
     */
    private String runCommand;

    /**
     * Хранит пути к файлам, который будут загружены через ;
     * Например, in/file.txt;in/file.json
     * Если это поле не задано, то входных данных в виде файлов программа не имеет
     */
    private String inFiles;

    /**
     * Хранит пути к файлам, который будут выгружены через ;
     * Например, out/file.txt;out/file.json
     * Если это поле не задано, то выходных данных в виде файлов программа не имеет
     */
    private String outFiles;

    /**
     * Идентификатор конфигурации, на основе которой происходит сборка
     */
    private Integer configID;

    /**
     * Директория внутри архива. Необходима для того, чтобы в нее перейти после разархирования
     * Сборка идет из корня проекта.
     */
    private String archiveInnerDir;

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

    public Integer getConfigID() {
        return configID;
    }

    public void setConfigID(Integer configID) {
        this.configID = configID;
    }

    public String getArchiveInnerDir() {
        return archiveInnerDir;
    }

    public void setArchiveInnerDir(String archiveInnerDir) {
        this.archiveInnerDir = archiveInnerDir;
    }
}
