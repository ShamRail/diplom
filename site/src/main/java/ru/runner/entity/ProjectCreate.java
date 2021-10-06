package ru.runner.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class ProjectCreate {
    @Getter
    @Setter
    private Project project;

    @Getter
    @Setter
    private MultipartFile logo;

    @Getter
    @Setter
    private MultipartFile sourceFile;
}
