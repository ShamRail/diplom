package ru.ugasu.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ugasu.app.model.Language;

//@RepositoryRestResource(collectionResourceRel = "languages", path = "languages")
public interface LanguageRepository extends JpaRepository<Language, Integer> {
}
