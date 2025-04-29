package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.OtherException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class BaseDbStorage<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> rowMapper;

    protected Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, rowMapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, rowMapper, params);
    }

    protected Long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;}, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        if (id != null) {
            return id;
        } else {
            throw new OtherException("Не удалось сохранить данные");
        }
    }

    protected void insertWithoutGeneratedKeys(String query, Object... params) {
        jdbc.update(query, params);
    }

    protected void manyInsert(String query, Set<Long> objectIds, Long relatedObjectId) {
        List<Object[]> batchArgs = objectIds.stream()
                .map(genreId -> new Object[]{relatedObjectId, genreId})
                .toList();
        jdbc.batchUpdate(query, batchArgs);
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);

        if (rowsUpdated == 0) {
            throw new OtherException("Не удалось обновить данные");
        }
    }

    protected void delete(String query, Object... params) {
        jdbc.update(query, params);
    }
}
