package ru.runner.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Validated
public class Language {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @NotEmpty
    private String name;

    @Getter
    @Setter
    @Type(type = "text")
    @NotEmpty
    private String version;

    @Getter
    @Setter
    @Type(type = "text")
    private String logo;

    @Override
    public String toString() {
        return name + " " + version;
    }
}
