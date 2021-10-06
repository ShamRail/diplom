package ru.runner.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class ProjectConfig {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Getter
    @Setter
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "builder_id", nullable = false)
    private Builder builder;

    public void generateName() {
        this.name = "Язык: " +
                language.getName() + " (ver. " +
                language.getVersion() + "); Сборщик: " +
                builder.getName() + " (ver. " +
                builder.getVersion() + ");";
    }
}
