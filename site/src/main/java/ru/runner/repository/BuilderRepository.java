package ru.runner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.runner.entity.Builder;

import java.util.List;

@Repository
public interface BuilderRepository extends JpaRepository<Builder, Long> {

    Builder findBuilderById(long id);

    List<Builder> findAllByLanguage_Id(long id);

}
