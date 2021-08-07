package ru.ugasu.app.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ugasu.app.model.run.App;

public interface AppRepository extends JpaRepository<App, Integer> {

}
