package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmRatingService {
    private final RatingDbStorage ratingDbStorage;

    public Collection<FilmRating> getFilmRatings() {
        return ratingDbStorage.getAllRatings();
    }

    public FilmRating getFilmRating(Long id) {
        return ratingDbStorage.getRating(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг с id " + id + " не найден"));
    }
}
