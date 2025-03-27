package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;


    public Collection<Film> getAllFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilm(id).orElseThrow(() -> new NotFoundException("Фильм с id  " + id + " не найден"));
    }

    public Film createFilm(Film film) {
        log.info("Добавлен фильм: {}", film);
        return filmStorage.addFilm(film);

    }

    public Film updateFilm(Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("В запросе на обновление фильма не передан id");
            throw new ValidationException("Id не может быть пустым");
        }

        filmStorage.getFilm(newFilm.getId())
                .orElseThrow(() -> {
                    log.warn("В запросе на обновление фильма передан неизвестный id - {}", newFilm.getId());
                    return new NotFoundException("Фильм с id " + newFilm.getId() + " не найден");
                });

        return filmStorage.updateFilm(newFilm);
    }

    public void likeFilm(Long filmId, Long userId) {
        if (!getFilmById(filmId).getUsersLikes().add(userService.getUserById(userId).getId())) {
            log.warn("Пользователь с id {} уже ставил лайк фильму с id {}", userId, filmId);
            throw new OtherException("Пользователь уже ставил лайк фильму");
        }

        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
    }
}
