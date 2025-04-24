package ru.yandex.practicum.filmorate.storage.rating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.RatingRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingDbStorage extends BaseDbStorage<FilmRating> implements RatingStorage {

    private static final String FIND_BY_ID = "SELECT * FROM rating WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM rating";

    public RatingDbStorage(JdbcTemplate jdbc, RatingRowMapper mapper) {
        super(jdbc, mapper);
    }

    public Optional<FilmRating> getRating(Long id) {
        return findOne(FIND_BY_ID, id);
    }

    public List<FilmRating> getAllRatings() {
        return findMany(FIND_ALL);
    }
}
