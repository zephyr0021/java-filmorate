package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(path = "/films")
public class FilmController {
    private final HashMap<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) throw new ValidationException("Id не может быть пустым");
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            Optional.ofNullable(newFilm.getName()).ifPresent(oldFilm::setName);
            Optional.ofNullable(newFilm.getDescription()).ifPresent(oldFilm::setDescription);
            Optional.ofNullable(newFilm.getReleaseDate()).ifPresent(oldFilm::setReleaseDate);
            Optional.ofNullable(newFilm.getDuration()).ifPresent(oldFilm::setDuration);
            return oldFilm;
        } else {
            throw new NotFoundException("Фильм с id " + newFilm.getId() + " не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
