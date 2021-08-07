package ru.ugasu.app.service.build;

import ru.ugasu.app.model.build.Build;
import ru.ugasu.app.model.build.Project;

public interface BuildTask {
    Build build(Project project);
}
