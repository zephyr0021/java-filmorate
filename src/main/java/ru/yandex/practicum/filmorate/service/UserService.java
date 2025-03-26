package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUser(id).orElseThrow(() -> new NotFoundException("Юзер с id  " + id + " не найден"));
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("В запросе создания пользователя передан пустой name. Для поля name использован логин {}",
                    user.getLogin());
        }

        return userStorage.addUser(user);
    }

    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            log.warn("В запросе на обновление юзера не передан id");
            throw new ValidationException("Id не может быть пустым");
        }

        userStorage.getUser(newUser.getId())
                .orElseThrow(() -> {
                    log.warn("В запросе на обновление пользователя передан неизвестный id - {}", newUser.getId());
                    return new NotFoundException("Пользователь с id " + newUser.getId() + " не найден");
                });

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
            log.info("В запросе обновления пользователя передан пустой name. Для поля name использован логин {}",
                    newUser.getLogin());
        }

        return userStorage.updateUser(newUser);


    }
}
