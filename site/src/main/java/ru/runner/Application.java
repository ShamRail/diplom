package ru.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.runner.entity.Role;
import ru.runner.repository.RoleRepository;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

    private final RoleRepository roleRepository;

    public Application(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void prepareRoleData() {
        if (roleRepository.findById(1L).isPresent()) {
            return;
        }
        roleRepository.save(new Role(1L, "ROLE_USER"));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}