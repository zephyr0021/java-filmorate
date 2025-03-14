package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.warn("В запросе на обновление юзера не передан id");
            throw new ValidationException("Id не может быть пустым");
        }
        if (userService.getUsersMap().containsKey(newUser.getId())) {
            User oldUser = userService.getUsersMap().get(newUser.getId());
            if (newUser.getName() == null || newUser.getName().isBlank()) {
                oldUser.setName(newUser.getLogin());
                log.info("В запросе обновления пользователя передан пустой name. Для поля name использован логин {}",
                        newUser.getLogin());
            } else {
                Optional.of(newUser.getName()).ifPresent(oldUser::setName);
            }
            Optional.ofNullable(newUser.getEmail()).ifPresent(oldUser::setEmail);
            Optional.ofNullable(newUser.getBirthday()).ifPresent(oldUser::setBirthday);
            Optional.ofNullable(newUser.getLogin()).ifPresent(oldUser::setLogin);
            log.info("Обновлен юзер: {}", oldUser);
            return oldUser;
        } else {
            log.warn("В запросе на обновление пользователя передан неизвестный id - {}", newUser.getId());
            throw new NotFoundException("Пользователь с id " + newUser.getId() + " не найден");
        }
    }
}
