package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier
public class FilmDbStorage extends BaseDbStorage<Film> {
    private static final String FIND_BY_ID = "SELECT f.*, " +
            "ARRAY_AGG(DISTINCT fg.genre_id) FILTER (WHERE fg.genre_id IS NOT NULL) AS genres, " +
            "ARRAY_AGG(DISTINCT fl.user_id) FILTER (WHERE fl.user_id IS NOT NULL) AS likes " +
            "FROM films AS f " +
            "LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
            "LEFT JOIN film_likes as fl ON f.id = fl.film_id " +
            "WHERE f.id = ? " +
            "GROUP BY f.id;";

    private static final String FIND_ALL = "SELECT f.*, " +
            "ARRAY_AGG(DISTINCT fg.genre_id) FILTER (WHERE fg.genre_id IS NOT NULL) AS genres, " +
            "ARRAY_AGG(DISTINCT fl.user_id) FILTER (WHERE fl.user_id IS NOT NULL) AS likes " +
            "FROM films AS f " +
            "LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
            "LEFT JOIN film_likes as fl ON f.id = fl.film_id " +
            "GROUP BY f.id;";

    private static final String INSERT = "INSERT INTO films (name, description, release_date, duration, rating_id) " +
            "VALUES (?, ?, ?, ?, ?);";
    private static final String INSERT_FILM_GENRES = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?);";


    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
    }

    public Optional<Film> getFilm(Long id) {
        return findOne(FIND_BY_ID, id);
    }

    public List<Film> getFilms() {
        return findMany(FIND_ALL);
    }

    public Film addFilm(Film film) {
        long id = insert(
                INSERT,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRatingId()
        );

        List<Long> genreIds = film.getGenreIds();

        if (genreIds != null) {
            manyInsert(INSERT_FILM_GENRES, genreIds, id);
        }
        film.setId(id);
        return film;
    }




}
