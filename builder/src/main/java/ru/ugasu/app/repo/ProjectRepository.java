package ru.ugasu.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ugasu.app.model.build.BuildStatus;
import ru.ugasu.app.model.build.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            value = "update Project p set p.buildStatus = :status where p.id = :id"
    )
    int updateStatusById(@Param("id") int id, @Param("status") BuildStatus buildStatus);

}
