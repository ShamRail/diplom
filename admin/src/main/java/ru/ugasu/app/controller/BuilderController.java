package ru.ugasu.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ugasu.app.model.Builder;
import ru.ugasu.app.model.Language;
import ru.ugasu.app.model.dto.BuilderDTO;
import ru.ugasu.app.repo.BuilderRepository;
import ru.ugasu.app.repo.LanguageRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/builders")
public class BuilderController {

    private BuilderRepository builderRepository;

    private LanguageRepository languageRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public BuilderController(BuilderRepository builderRepository,
                             LanguageRepository languageRepository,
                             EntityManager entityManager) {
        this.builderRepository = builderRepository;
        this.languageRepository = languageRepository;
        this.entityManager = entityManager;
    }

    @PostMapping
    public Builder save(@RequestBody BuilderDTO builderDTO) {
        List<Language> languages = checkAndGetLanguage(builderDTO.getLanguageID());
        return builderRepository.save(new Builder(
                builderDTO.getName(), builderDTO.getVersion(), languages
        ));
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody BuilderDTO builderDTO) {
        if (!builderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid builder id");
        }
        List<Language> languages = checkAndGetLanguage(builderDTO.getLanguageID());
        builderRepository.save(new Builder(
                id, builderDTO.getName(), builderDTO.getVersion(), languages
        ));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        if (!builderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid builder id");
        }
        builderRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    public Builder findById(@PathVariable int id) {
        try {
            return entityManager.createQuery(
                    "SELECT b FROM Builder AS b JOIN FETCH b.languages WHERE b.id=:id", Builder.class)
                            .setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid builder id");
        }
    }

    @GetMapping
    public List<Builder> findAll() {
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
