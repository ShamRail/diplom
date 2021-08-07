package ru.ugasu.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ugasu.app.model.Language;
import ru.ugasu.app.repo.LanguageRepository;

import java.util.List;

@RestController
@RequestMapping("/languages")
public class LanguageController {

    private final LanguageRepository languageRepository;

    public LanguageController(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @PostMapping
    public Language save(@RequestBody Language language) {
        return languageRepository.save(language);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Language language) {
        if (!languageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Language with specified id not found!");
        }
        language.setId(id);
        languageRepository.save(language);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        if (!languageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Language with specified id not found!");
        }
        languageRepository.deleteById(id);
    }

    @GetMapping
    public List<Language> findAll() {
        return languageRepository.findAll();
    }

    @GetMapping("/{id}")
    public Language findById(@PathVariable int id) {
        return languageRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Language with specified id not found!")
        );
    }

}
