package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> getFilms() {
        return films.values();
    }

    public Map<Long, Film> getFilmsMap() {
        return films;
    }

    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    public Film updateFilm(Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("В запросе на обновление фильма не передан id");
            throw new ValidationException("Id не может быть пустым");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
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

    public void clearData() {
        films.clear();
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
