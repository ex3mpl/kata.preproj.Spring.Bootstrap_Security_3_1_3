package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class Init {

    private final UserService userService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder encoder;

    public Init(UserService userService, RoleService roleService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @PostConstruct
    public void init() {
        // Проверка и создание ролей
        Role adminRole = roleService.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setId(1);
            roleService.save(adminRole);
        }

        Role userRole = roleService.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setId(2);
            roleService.save(userRole);
        }

        // Проверка и создание пользователя admin с ролью ROLE_ADMIN
        if (userService.findByUsername("admin") == null) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(encoder.encode("admin")); // Зашифрованный пароль
            adminUser.setEmail("admin@bk.ru");
            adminUser.setAge(20);
            adminUser.setId(1);
            adminUser.setRoles(new HashSet<>(Set.of(adminRole))); // Назначаем только роль ROLE_ADMIN
            userService.save(adminUser);
        }

        // Проверка и создание пользователя user с ролью ROLE_USER
        if (userService.findByUsername("user") == null) {
            User normalUser = new User();
            normalUser.setUsername("user");
            normalUser.setPassword(encoder.encode("user")); // Зашифрованный пароль
            normalUser.setEmail("user@bk.ru");
            normalUser.setAge(30);
            normalUser.setId(2);
            normalUser.setRoles(new HashSet<>(Set.of(userRole))); // Назначаем только роль ROLE_USER
            userService.save(normalUser);
        }
    }
}
