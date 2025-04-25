package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable Long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }
//
//    @PutMapping("/{id}/like/{userId}")
//    public SuccessResponse likeFilm(@PathVariable Long id, @PathVariable Long userId) {
//        filmService.likeFilm(id, userId);
//        return new SuccessResponse();
//    }
//
//    @DeleteMapping("/{id}/like/{userId}")
//    public SuccessResponse removeLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
//        filmService.removeLikeFilm(id, userId);
//        return new SuccessResponse();
//    }
//
//    @GetMapping("/popular")
//    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
//        return filmService.getPopularFilms(count);
//    }
}
