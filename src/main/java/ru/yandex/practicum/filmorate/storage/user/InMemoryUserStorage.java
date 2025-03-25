package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getUsers() {
        return users.values();
    }

    public User getUser(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Юзер с id  " + id + " не найден");
        }
        return users.get(id);
    }

    public User addUser(User user) {
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

    public User updateUser(User newUser) {
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

    public void clearData() {
        users.clear();
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
