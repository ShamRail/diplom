package ru.ugasu.app.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Конфигурация сборки. Сборка зависит от языка программирования и сборщика проекта.
 * Сборщик и ЯП представляют лишь описание конфигурации.
 * Настоящее влияние на сборку оказывает Dockerfile.
 */
@Entity
@Table(name = "configuration")
public class Configuration extends Versioned {

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "build_id")
    private Builder builder;

    /**
     * Описание конфигурации. Сводная информация
     */
    @Column(name = "description")
    private String description;

    /**
     * Путь к Dockerfile
     */
    @Column(name = "docker_file")
    private String dockerFile;

    public Configuration() { }

    public Configuration(String name, String version,
                         Language language, Builder builder,
                         String description, String dockerFile) {
        super(name, version);
        this.language = language;
        this.builder = builder;
        this.description = description;
        this.dockerFile = dockerFile;
    }

    public Configuration(int id,
                         String name, String version, Language language, Builder builder, String description, String dockerFile) {
        super(id, name, version);
        this.language = language;
        this.builder = builder;
        this.description = description;
        this.dockerFile = dockerFile;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDockerFile() {
        return dockerFile;
    }

    public void setDockerFile(String dockerFile) {
        this.dockerFile = dockerFile;
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
        Configuration that = (Configuration) o;
        return Objects.equals(language, that.language) && Objects.equals(builder, that.builder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), language, builder);
    }
}
