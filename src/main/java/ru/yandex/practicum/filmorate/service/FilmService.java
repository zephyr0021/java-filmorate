package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmGenreService filmGenreService;
    private final MpaService mpaService;
    private final UserService userService;
    private final Comparator<Film> filmComparator = Comparator.comparing((Film film) -> film.getUsersLikes().size()).reversed();

    public FilmService(UserService userService, MpaService mpaService, FilmGenreService filmGenreService,
                       @Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.userService = userService;
        this.mpaService = mpaService;
        this.filmGenreService = filmGenreService;
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilm(id).orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
    }

    public Film createFilm(Film film) {
        Film validationFilm = validateAndSetInfoMpaAndGenres(film);
        return filmStorage.addFilm(validationFilm);
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

        Film newValidationFilm = validateAndSetInfoMpaAndGenres(newFilm);

        return filmStorage.updateFilm(newValidationFilm);
    }

    public void likeFilm(Long filmId, Long userId) {
        if (getFilmById(filmId).getUsersLikes().contains(userService.getUserById(userId).getId())) {
            log.warn("Пользователь с id {} уже ставил лайк фильму с id {}", userId, filmId);
            throw new OtherException("Пользователь уже ставил лайк фильму");
        }

        filmStorage.addLikeFilm(filmId, userId);
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
    }

    public void removeLikeFilm(Long filmId, Long userId) {
        if (!getFilmById(filmId).getUsersLikes().contains(userService.getUserById(userId).getId())) {
            log.warn("Пользователь с id {} не ставил лайк фильму id {}", userId, filmId);
            throw new OtherException("Пользователь не ставил лайк фильму");
        }

        filmStorage.deleteLikeFilm(filmId, userId);
        log.info("Пользователь с id {} удалил свой лайк у фильма с id {}", userId, filmId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getFilms().stream().sorted(filmComparator).limit(count).toList();
    }

    private Film validateAndSetInfoMpaAndGenres(Film film) {
        Set<FilmGenre> filmGenres = film.getGenres();
        Mpa mpa = film.getMpa();

        if (mpa != null) {
            Long mpaId = mpa.getId();
            mpaService.getMpa(mpaId);
            mpa.setName(mpaService.getMpa(mpaId).getName());
        }

        if (filmGenres != null && !filmGenres.isEmpty()) {
            filmGenres.forEach(genre -> filmGenreService.getGenreById(genre.getId()));

            film.setGenres(filmGenres.stream()
                    .peek(genre -> genre.setName(filmGenreService.getGenreById(genre.getId()).getName()))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        return film;
    }

    public void clearFilmsData() {
        filmStorage.clearData();
    }
}
