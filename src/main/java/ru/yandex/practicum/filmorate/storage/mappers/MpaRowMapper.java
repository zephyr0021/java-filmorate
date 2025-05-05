package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<Mpa> {

    @Override
    public Mpa mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Mpa mpa = new Mpa();

        mpa.setId(rs.getLong("id"));
        mpa.setName(rs.getString("title"));

        return mpa;
    }
}
