package ru.ugasu.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.ugasu.app.model.Builder;
import ru.ugasu.app.model.Language;
import ru.ugasu.app.model.dto.BuilderDTO;
import ru.ugasu.app.repo.BuilderRepository;
import ru.ugasu.app.repo.LanguageRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/builders")
public class BuilderController {

    private final BuilderRepository builderRepository;

    private final LanguageRepository languageRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public BuilderController(BuilderRepository builderRepository,
                             LanguageRepository languageRepository,
                             EntityManager entityManager) {
        this.builderRepository = builderRepository;
        this.languageRepository = languageRepository;
        this.entityManager = entityManager;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public @ResponseBody Builder save(@ModelAttribute BuilderDTO.WithFile builderDTO) {
        List<Language> languages = checkAndGetLanguage(builderDTO.getLanguageID());
        Builder builder = new Builder(
                builderDTO.getName(), builderDTO.getVersion(), languages
        );
        convertToBase64(builderDTO.getFile(), builder);
        return builderRepository.save(builder);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable int id,
                       @ModelAttribute BuilderDTO builderDTO,
                       @RequestParam(required = false) MultipartFile file
    ) {
        var builderDb = builderRepository.findById(id);
        if (builderDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid builder id");
        }
        List<Language> languages = checkAndGetLanguage(builderDTO.getLanguageID());
        Builder builder = new Builder(
                id, builderDTO.getName(), builderDTO.getVersion(), languages
        );
        builder.setLogo(builderDb.get().getLogo());
        convertToBase64(file, builder);
        builderRepository.save(builder);
    }

    private void convertToBase64(@RequestParam("file") MultipartFile file, Builder builder) {
        if (file != null) {
            try {
                builder.setLogo(Base64.getEncoder().encodeToString(file.getBytes()));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong during handling file content");
            }
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        if (!builderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid builder id");
        }
        builderRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    public @ResponseBody Builder findById(@PathVariable int id) {
        try {
            return entityManager.createQuery(
                    "SELECT b FROM Builder AS b JOIN FETCH b.languages WHERE b.id=:id", Builder.class)
                            .setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid builder id");
        }
    }

    @GetMapping
    public @ResponseBody List<Builder> findAll() {
        return entityManager.createQuery(
                "SELECT b FROM Builder AS b JOIN FETCH b.languages", Builder.class)
                .getResultStream().collect(Collectors.toList());
    }

    private List<Language> checkAndGetLanguage(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return Collections.emptyList();
        }
        List<Language> languages = languageRepository
                .findAllById(Arrays.stream(ids).filter(Objects::nonNull).collect(Collectors.toList()));
        if (languages.size() != ids.length) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One of languages has invalid id");
        }
        return languages;
    }

}
