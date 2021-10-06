package ru.runner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.runner.entity.Builder;
import ru.runner.entity.Language;
import ru.runner.repository.BuilderRepository;
import ru.runner.repository.LanguageRepository;

import java.util.List;

@Service
public class LanguageAndBuilderService {

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    BuilderRepository builderRepository;

    public List<Builder> getBuildersForLanguage(long languageId) {
        return builderRepository.findAllByLanguage_Id(languageId);
    }

    public List<Builder> getAllBuilders() {
        return builderRepository.findAll();
    }

    public List<Language> getAllLanguages(){
        return languageRepository.findAll();
    }

    public Language getLanguageById(long id) {
        return languageRepository.getOne(id);
    }

    public void addNewLanguage(Language language) {
        languageRepository.save(language);
    }

    public void addNewBuilder(Builder builder) {
        builderRepository.save(builder);
    }

    public boolean deleteLanguage(long languageId) {
        boolean ifExist = languageRepository.existsById(languageId);

        if (!ifExist) {
            return false;
        }

        List<Builder> builderList = builderRepository.findAllByLanguage_Id(languageId);

        for (Builder builder : builderList) {
            builderRepository.delete(builder);
        }

        languageRepository.deleteById(languageId);

        return true;
    }

    public boolean deleteBuilder(Long builderId){
        boolean ifExist = builderRepository.existsById(builderId);

        if (!ifExist) {
            return false;
        }
        builderRepository.deleteById(builderId);

        return true;
    }
}
