package ru.ugasu.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ugasu.app.model.Builder;

//@RepositoryRestResource(collectionResourceRel = "builders", path = "builders")
public interface BuilderRepository extends JpaRepository<Builder, Integer> {
}
