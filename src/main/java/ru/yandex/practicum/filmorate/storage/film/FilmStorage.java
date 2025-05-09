package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getFilms();

    Optional<Film> getFilm(Long id);

    void addLikeFilm(Long filmId, Long userId);

    void deleteLikeFilm(Long filmId, Long userId);

    void clearData();


}
