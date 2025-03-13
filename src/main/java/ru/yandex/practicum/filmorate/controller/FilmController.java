package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/films")
public class FilmController {

    FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("В запросе на обновление фильма не передан id");
            throw new ValidationException("Id не может быть пустым");
        }
        if (filmService.getFilmsMap().containsKey(newFilm.getId())) {
            Film oldFilm = filmService.getFilmsMap().get(newFilm.getId());
            Optional.ofNullable(newFilm.getName()).ifPresent(oldFilm::setName);
            Optional.ofNullable(newFilm.getDescription()).ifPresent(oldFilm::setDescription);
            Optional.ofNullable(newFilm.getReleaseDate()).ifPresent(oldFilm::setReleaseDate);
            Optional.ofNullable(newFilm.getDuration()).ifPresent(oldFilm::setDuration);
            log.info("Обновлен фильм: {}", oldFilm);
            return oldFilm;
        } else {
            log.warn("В запросе на обновление фильма передан неизвестный id - {}", newFilm.getId());
            throw new NotFoundException("Фильм с id " + newFilm.getId() + " не найден");
        }
    }
}
