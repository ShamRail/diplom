package ru.ugasu.app.model;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность представляет сборщика проекта, который производит сборку проекта при сборке Docker образа.
 * Например, maven. Через команду ниже
 * <pre>
 * {@code
 *      mvn install
 * }
 * </pre>
 */
@Entity
@Table(name = "builder")
public class Builder extends Versioned {

    @ManyToMany
    @JoinTable(
            name = "language_builder",
            joinColumns = {@JoinColumn(name = "builder_id")},
            inverseJoinColumns = {@JoinColumn(name = "language_id")}
    )
    private List<Language> languages;

    @Column(name = "logo")
    private String logo;

    public Builder() { }

    public Builder(String name, String version) {
        super(name, version);
    }

    public Builder(String name, String version, List<Language> languages) {
        super(name, version);
        this.languages = languages;
    }

    public Builder(int id, String name, String version, List<Language> languages) {
        super(id, name, version);
        this.languages = languages;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
