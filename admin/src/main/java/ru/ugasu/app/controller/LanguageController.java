package ru.ugasu.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.ugasu.app.model.Language;
import ru.ugasu.app.repo.LanguageRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/languages")
public class LanguageController {

    private final LanguageRepository languageRepository;

    public LanguageController(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @PostMapping(
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public @ResponseBody Language save(
            @RequestParam("name") String name,
            @RequestParam("version") String version,
            @RequestParam("file") MultipartFile file) {
        var language = new Language(name, version);
        convertToBase64(language, file);
        return languageRepository.save(language);
    }

    private void convertToBase64(@RequestBody Language language, @RequestParam("file") MultipartFile file) {
        if (file != null) {
            try {
                language.setLogo(Base64.getEncoder().encodeToString(file.getBytes()));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong during handling file content");
            }
        }
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> update(@PathVariable int id,
                                 @RequestParam("name") String name,
                                 @RequestParam("version") String version,
                                 @RequestParam(value = "file", required = false) MultipartFile file) {
        var language = new Language(name, version);
        var dbLanguage = languageRepository.findById(id);
        if (dbLanguage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Language with specified id not found!");
        }
        language.setId(id);
        language.setLogo(dbLanguage.get().getLogo());
        convertToBase64(language, file);
        languageRepository.save(language);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!languageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Language with specified id not found!");
        }
        languageRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public @ResponseBody List<Language> findAll() {
        return languageRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody Language findById(@PathVariable int id) {
        return languageRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Language with specified id not found!")
        );
    }

}
