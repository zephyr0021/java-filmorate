package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingRowMapper implements RowMapper<FilmRating> {

    @Override
    public FilmRating mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final FilmRating filmRating = new FilmRating();

        filmRating.setId(rs.getLong("id"));
        filmRating.setTitle(rs.getString("title"));

        return filmRating;
    }
}
