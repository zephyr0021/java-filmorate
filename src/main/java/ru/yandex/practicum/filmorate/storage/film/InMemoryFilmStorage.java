package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> getFilms() {
        return films.values();
    }

    public Optional<Film> getFilm(Long id) {
        return films.values().stream()
                .filter(film -> film.getId().equals(id))
                .findFirst();
    }

    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    public Film updateFilm(Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());
        Optional.ofNullable(newFilm.getName()).ifPresent(oldFilm::setName);
        Optional.ofNullable(newFilm.getDescription()).ifPresent(oldFilm::setDescription);
        Optional.ofNullable(newFilm.getReleaseDate()).ifPresent(oldFilm::setReleaseDate);
        Optional.ofNullable(newFilm.getDuration()).ifPresent(oldFilm::setDuration);
        log.info("Обновлен фильм: {}", newFilm);
        return oldFilm;
    }

    public void addLikeFilm(Long filmId, Long userId) {
        films.get(filmId).getUsersLikes().add(userId);
    }

    public void deleteLikeFilm(Long filmId, Long userId) {
        films.get(filmId).getUsersLikes().remove(userId);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public void clearData() {
        films.clear();
    }
}
