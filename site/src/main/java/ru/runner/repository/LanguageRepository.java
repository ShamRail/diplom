package ru.runner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.runner.entity.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language findLanguageById(long id);
    Language findLanguageByName(String name);
}
