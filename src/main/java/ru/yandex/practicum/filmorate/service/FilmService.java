package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final FilmGenreService filmGenreService;
    private final MpaService filmRatingService;
    private final UserService userService;
    private final Comparator<Film> filmComparator = Comparator.comparing((Film film) -> film.getUsersLikes().size()).reversed();


    public Collection<Film> getAllFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilm(id).orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
    }

    public Film createFilm(Film film) {
        List<Long> filmGenres = film.getGenres();
        Long ratingId = film.getMpa();
        if (ratingId != null) {
            filmRatingService.getFilmRating(ratingId);
        }

        if (filmGenres != null ) {
            filmGenres.forEach(filmGenreService::getGenreById);
        } else {
            film.setGenres(List.of());
        }

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

        Long ratingId = newFilm.getMpa();
        List<Long> filmGenres = newFilm.getGenres();

        if (ratingId != null) {
            filmRatingService.getFilmRating(ratingId);
        }

        if (filmGenres != null ) {
            filmGenres.forEach(filmGenreService::getGenreById);
        } else {
            newFilm.setGenres(List.of());
        }

        return filmStorage.updateFilm(newFilm);
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
//
//    public void clearFilmsData() {
//        filmStorage.clearData();
//    }
}
