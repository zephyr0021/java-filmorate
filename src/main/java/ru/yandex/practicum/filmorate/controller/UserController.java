package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final HashMap<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("В запросе создания пользователя передан пустой name. Для поля name использован логин {}",
                    user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.warn("В запросе на обновление юзера не передан id");
            throw new ValidationException("Id не может быть пустым");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
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

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
