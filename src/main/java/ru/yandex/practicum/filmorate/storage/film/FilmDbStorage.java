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
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
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

    private static final String INSERT_LIKE_FILM = "INSERT INTO film_likes (user_id, film_id) VALUES (?, ?);";

    private static final String UPDATE_FILM = "UPDATE films SET name = ?, description = ?, " +
            "release_date = ?, duration = ?, rating_id = ? WHERE id = ?;";

    private static final String DELETE_GENRES = "DELETE FROM film_genre WHERE film_id = ?;";

    private static final String DELETE_ALL = "DELETE FROM films;";

    private static final String DELETE_LIKE_FILM = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?;";

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

    public Film updateFilm(Film film) {
        update(
                UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRatingId(),
                film.getId()
        );

        delete(DELETE_GENRES, film.getId());

        List<Long> genreIds = film.getGenreIds();

        if (genreIds != null) {
            manyInsert(INSERT_FILM_GENRES, genreIds, film.getId());
        }

        return film;
    }

    public void addLikeFilm(Long filmId, Long userId) {
        insertWithoutGeneratedKeys(INSERT_LIKE_FILM, userId, filmId);
    }

    public void deleteLikeFilm(Long filmId, Long userId) {
        delete(DELETE_LIKE_FILM, filmId, userId);
    }

    public void clearData() {
        delete(DELETE_ALL);
    }

}