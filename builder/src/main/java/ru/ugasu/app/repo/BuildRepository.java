package ru.ugasu.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ugasu.app.model.build.Build;
import ru.ugasu.app.model.build.BuildStatus;
import ru.ugasu.app.model.build.Project;

import java.util.Optional;

public interface BuildRepository extends JpaRepository<Build, Integer> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Build b set b.buildStatus = :status where b.id = :id")
    int updateStatusById(@Param("id") int id, @Param("status") BuildStatus buildStatus);

    // TODO протестировать
    Optional<Build> findByProject(Project project);

    void deleteByProject(Project project);

}
