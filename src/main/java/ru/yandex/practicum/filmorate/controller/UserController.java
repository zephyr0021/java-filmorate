package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final InMemoryUserStorage userStorage;

    @GetMapping
    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        return userStorage.updateUser(newUser);
    }
}
