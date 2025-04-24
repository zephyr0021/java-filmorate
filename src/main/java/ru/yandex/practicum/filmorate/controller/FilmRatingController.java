package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.service.FilmRatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class FilmRatingController {

    private final FilmRatingService filmRatingService;

    @GetMapping
    public Collection<FilmRating> getRatings() {
        return filmRatingService.getFilmRatings();
    }

    @GetMapping("/{ratingId}")
    public FilmRating getRating(@PathVariable Long ratingId) {
        return filmRatingService.getFilmRating(ratingId);
    }
}
