package ru.runner.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "t_project")
public class Project {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "config_id")
    private ProjectConfig projectConfig;

    @Getter
    @Setter
    @Size(min = 3, message = "Минимум 3 символа")
    private String name;

    @Getter
    @Setter
    @Type(type = "text")
    private String sourceUrl;

    @Getter
    @Setter
    @Type(type = "text")
    private String status;

    @Getter
    @Setter
    @Type(type = "text")
    private String description;

    @Getter
    @Setter
    @Type(type = "text")
    private String logo;

    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date creatingDate;
}
