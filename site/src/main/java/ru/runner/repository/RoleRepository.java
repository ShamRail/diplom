package ru.runner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.runner.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
