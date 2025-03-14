package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getUsers() {
        return users.values();
    }

    public Map<Long, User> getUsersMap() {
        return users;
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
