package ru.runner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.runner.entity.Builder;
import ru.runner.entity.Language;
import ru.runner.entity.ProjectConfig;
import ru.runner.service.LanguageAndBuilderService;
import ru.runner.service.ProjectConfigService;
import ru.runner.service.ProjectService;
import ru.runner.service.UserService;

import javax.validation.Valid;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectConfigService configService;

    @Autowired
    private LanguageAndBuilderService languageAndBuilderService;

    @GetMapping("/about")
    public String aboutUsPage(Model model) {
        return "/about_us";
    }

    @GetMapping("/admin/users")
    public String userList(Model model) {
        model.addAttribute("userList", userService.allUsers());
        return "/admin/manage_users";
    }

    @GetMapping("/admin/projects")
    public String projectList(Model model) {
        model.addAttribute("projectList", projectService.getAllProjects());
        return "/admin/manage_projects";
    }

    @PostMapping("/admin/users/delete")
    public String deleteUser(@RequestParam(required = true, defaultValue = "") Long userId,
                             Model model) {

        if (userId != null) {
            userService.deleteUser(userId);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/project/delete")
    public String deleteProject(@RequestParam(required = true, defaultValue = "") Long projectId,
                                Model model) {

        if (projectId != null) {
            projectService.deleteProject(projectId);
        }
        return "redirect:/admin/projects";
    }

    @GetMapping("/admin/configs")
    public String showConfig(Model model) {
        model.addAttribute("configList", configService.getAllConfigs());
        return "/admin/manage_configs";
    }

    @GetMapping("/admin/configs/create")
    public String createConfig(Model model) {
        model.addAttribute("configForm", new ProjectConfig());
        model.addAttribute("languageList", languageAndBuilderService.getAllLanguages());

        return "/admin/create_config";
    }

    @PostMapping("/admin/configs/create")
    public String createConfigSelectBuilder(@ModelAttribute("configForm") @Valid ProjectConfig config,
                                            Model model) {

        long langId = config.getLanguage().getId();

        model.addAttribute("builderList", languageAndBuilderService.getBuildersForLanguage(langId));
        model.addAttribute("configForm", config);

        return "/admin/create_config_select_builder";
    }

    @PostMapping("/admin/configs/builder")
    public String saveNewConfig(@ModelAttribute("configForm") @Valid ProjectConfig config,
                                Model model) {

        boolean isEmptyBuilder = config.getBuilder() == null;

        if (!isEmptyBuilder) {
            config.setLanguage(config.getBuilder().getLanguage());
            config.generateName();
            configService.addNewConfig(config);
        }

        return "redirect:/admin/configs";
    }

    @PostMapping("/admin/configs/delete")
    public String deleteConfig(@RequestParam(required = true, defaultValue = "") Long configId,
                               Model model) {

        if (configId != null) {
            configService.deleteConfig(configId);
        }

        return "redirect:/admin/configs";
    }

    @GetMapping("/admin/builders")
    public String showBuilders(Model model) {
        model.addAttribute("builderList", languageAndBuilderService.getAllBuilders());

        return "/admin/manage_builder";
    }

    @PostMapping("/admin/builders/delete")
    public String deleteBuilder(@RequestParam(required = true, defaultValue = "") Long builderId,
                                Model model) {

        if (builderId != null) {
            languageAndBuilderService.deleteBuilder(builderId);
        }

        return "redirect:/admin/builders";
    }

    @GetMapping("/admin/builders/create")
    public String createBuilder(Model model) {
        model.addAttribute("builderForm", new Builder());
        model.addAttribute("langList", languageAndBuilderService.getAllLanguages());
        return "/admin/create_builder";
    }

    @PostMapping("/admin/builders/create")
    public String saveNewBuilder(@ModelAttribute("builderForm") @Valid Builder builder,
                                 Model model) {
        languageAndBuilderService.addNewBuilder(builder);

        return "redirect:/admin/builders";
    }

    @GetMapping("/admin/languages")
    public String showLanguages(Model model) {
        model.addAttribute("langList", languageAndBuilderService.getAllLanguages());

        return "/admin/manage_language";
    }

    @PostMapping("/admin/languages/delete")
    public String deleteLanguage(@RequestParam(required = true, defaultValue = "") Long langId,
                                 Model model) {

        if (langId != null) {
            languageAndBuilderService.deleteLanguage(langId);
        }

        return "redirect:/admin/languages";
    }

    @GetMapping("/admin/languages/create")
    public String createLanguage(Model model) {
        model.addAttribute("langForm", new Language());

        return "admin/create_language";
    }

    @PostMapping("/admin/languages/create")
    public String saveNewLanguage(@ModelAttribute("langForm") @Valid Language language,
                                  Model model) {
        languageAndBuilderService.addNewLanguage(language);

        return "redirect:/admin/languages";
    }
}

