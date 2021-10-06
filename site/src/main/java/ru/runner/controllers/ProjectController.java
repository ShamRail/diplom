package ru.runner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.runner.entity.Project;
import ru.runner.entity.ProjectConfig;
import ru.runner.entity.ProjectCreate;
import ru.runner.entity.User;
import ru.runner.service.ProjectConfigService;
import ru.runner.service.ProjectService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class ProjectController {
    public static final String DEFAULT_PROJECT_LOGO = "410f86ca-23fd-4e14-8eac-d4e8e0100435. empty.png";

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectConfigService configService;

    @Value("${upload.path.img}")
    private String uploadPathImg;


    @Value("${upload.path.files}")
    private String uploadPathFiles;

    @GetMapping("/allProjects")
    public String showAllProjects(Model model) {
        List<Project> projectList = projectService.getAllProjects();

        model.addAttribute("projectList", projectList);

        return "project/all_projects";
    }

    @GetMapping("/project/{id}")
    public String showProject(Model model, @PathVariable("id") long id) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "project/project";
    }


    @GetMapping("/project/create")
    public String createProject(Model model) {
        List<ProjectConfig> configList = configService.getAllConfigs();

        model.addAttribute("configList", configList);
        model.addAttribute("projectForm", new ProjectCreate());
        model.addAttribute("isEdit", false);

        return "project/edit_project";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(
            MaxUploadSizeExceededException exc,
            HttpServletRequest request,
            HttpServletResponse response) {

        ModelAndView modelAndView = new ModelAndView("/project/edit_project");
        modelAndView.getModel().put("fileError", "Файл слишком большой!");
        return modelAndView;
    }

    @PostMapping("/project/create")
    public String createProject(@ModelAttribute("projectForm") @Valid ProjectCreate projectCreate,
                                BindingResult bindingResult,
                                Model model) throws Exception {

        if (bindingResult.hasErrors()) {
            return "/project/edit_project";
        }

        if (projectCreate.getProject().getName().isEmpty()) {
            model.addAttribute("nameError", "Название не может быть пустым!");
            return "/project/edit_project";
        }

        if (projectCreate.getSourceFile().getSize() > (30 * 1024 * 1024)) {
            model.addAttribute("fileError", "Файл не может быть больше !");
            return "/project/edit_project";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        String sourceUrl = saveFile(projectCreate.getSourceFile(), false, uploadPathFiles);
        String logo = saveFile(projectCreate.getLogo(), true, uploadPathImg);

        Project project = projectCreate.getProject();
        project.setUser(user);
        project.setCreatingDate(new Date());
        project.setLogo("");
        project.setLogo(logo);
        project.setSourceUrl(sourceUrl);

        if (bindingResult.hasErrors()) {
            throw new Exception("Не удалось создать проект, обратитесь к администратору. " + bindingResult.toString());
        }

        projectService.addNewProject(project);

        return "redirect:/allProjects";
    }

    private String saveFile(MultipartFile file, boolean isImg, String uploadPath) throws IOException {

        if (file != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            if (!file.getOriginalFilename().isEmpty()) {
                String uuidFile = UUID.randomUUID().toString();
                String resultFileName = uuidFile + ". " + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFileName));
                return resultFileName;
            }
        }

        return isImg ? DEFAULT_PROJECT_LOGO : "";
    }

    @GetMapping("/project/edit/{id}")
    public String showEditProjectForm(Model model, @PathVariable("id") long id) {

        Project project = projectService.getProjectById(id);
        ProjectCreate projectCreate = new ProjectCreate();
        projectCreate.setProject(project);

        model.addAttribute("projectForm", projectCreate);
        model.addAttribute("configList", configService.getAllConfigs());
        model.addAttribute("isEdit", true);
        return "project/edit_project";
    }

    @PostMapping("/project/edit/{id}")
    public String updateProject(@ModelAttribute("projectForm") @Valid ProjectCreate projectCreate,
                                @PathVariable("id") long id,
                                BindingResult bindingResult)
            throws Exception {

        if (bindingResult.hasErrors()) {
            throw new Exception("Не удалось обновить проект, обратитесь к администратору. " + bindingResult.toString());
        }

        Project updateProject = projectCreate.getProject();
        String logo = saveFile(projectCreate.getLogo(), true, DEFAULT_PROJECT_LOGO);
        String file = saveFile(projectCreate.getSourceFile(), false, "");

        updateProject.setLogo(logo);
        updateProject.setSourceUrl(file);
        projectService.updateProject(projectCreate.getProject());

        return "redirect:/project/" + id;
    }


    @PostMapping("/search")
    public String searchProject(@RequestParam(required = true, defaultValue = "") String searchText, Model model) {
        List<Project> projectList = projectService.searchProjectsByName(searchText);
        model.addAttribute("projectList", projectList);
        return "project/all_projects";
    }
}
