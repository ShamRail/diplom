package ru.ugasu.app.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ugasu.app.model.run.App;

import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Integer> {
    Optional<App> findByContainerID(String containerID);
}
