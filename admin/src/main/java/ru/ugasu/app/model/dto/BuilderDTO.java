package ru.ugasu.app.model.dto;

import org.springframework.web.multipart.MultipartFile;

public class BuilderDTO {

    private String name;

    private String version;

    private Integer[] languageID;

    public static class WithFile extends BuilderDTO {

        private MultipartFile file;

        public MultipartFile getFile() {
            return file;
        }

        public void setFile(MultipartFile file) {
            this.file = file;
        }

    }

    public BuilderDTO() { }

    public BuilderDTO(String name, String version, Integer[] languageID) {
        this.name = name;
        this.version = version;
        this.languageID = languageID;
    }

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

    public Integer[] getLanguageID() {
        return languageID;
    }

    public void setLanguageID(Integer[] languageID) {
        this.languageID = languageID;
    }

}
