package ru.ugasu.app.model.dto;

public class ConfigurationDTO {

    private String name;

    private String version;

    private Integer languageID;

    private Integer builderID;

    private String description;

    private String dockerContent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getLanguageID() {
        return languageID;
    }

    public void setLanguageID(Integer languageID) {
        this.languageID = languageID;
    }

    public Integer getBuilderID() {
        return builderID;
    }

    public void setBuilderID(Integer builderID) {
        this.builderID = builderID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDockerContent() {
        return dockerContent;
    }

    public void setDockerContent(String dockerContent) {
        this.dockerContent = dockerContent;
    }
}
