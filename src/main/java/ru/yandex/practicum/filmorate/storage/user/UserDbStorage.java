package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseDbStorage<User> {

    private static final String FIND_BY_ID = "SELECT u.*, " +
                    "ARRAY_AGG(DISTINCT f.friend_id) FILTER (WHERE f.friend_id IS NOT NULL) AS friends " +
                    "FROM users AS u " +
                    "LEFT JOIN friends AS f ON u.id = f.user_id " +
                    "WHERE u.id = ? " +
                    "GROUP BY u.id;";

    private static final String FIND_ALL = "SELECT u.*, " +
            "ARRAY_AGG(DISTINCT f.friend_id) FILTER (WHERE f.friend_id IS NOT NULL) AS friends " +
            "FROM users AS u " +
            "LEFT JOIN friends AS f ON u.id = f.user_id " +
            "GROUP BY u.id;";

    public UserDbStorage(JdbcTemplate jdbc, UserRowMapper mapper) {
        super(jdbc, mapper);
    }

    public Optional<User> getUser(Long id) {
        return findOne(FIND_BY_ID, id);
    }

    public List<User> getAllUsers() {
        return findMany(FIND_ALL);
    }
}
