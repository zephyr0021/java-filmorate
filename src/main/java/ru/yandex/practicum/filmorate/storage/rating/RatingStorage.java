package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.FilmRating;

import java.util.Collection;
import java.util.Optional;

public interface RatingStorage {

    Optional<FilmRating> getRating(Long id);

    Collection<FilmRating> getAllRatings();
}
