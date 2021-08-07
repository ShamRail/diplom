package ru.ugasu.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ugasu.app.model.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {
}
