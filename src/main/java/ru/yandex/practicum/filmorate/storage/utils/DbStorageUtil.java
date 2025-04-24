package ru.yandex.practicum.filmorate.storage.utils;

import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DbStorageUtil {

    public static Long getNullableLong(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }

    public static Integer getNullableInt(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : value;
    }

    public static LocalDate getNullableLocalDate(ResultSet rs, String columnName) throws SQLException {
        Date date = rs.getDate(columnName);
        return date == null ? null : date.toLocalDate();
    }

    public static List<Long> getNullableLongList(ResultSet rs, String columnName) throws SQLException {
        Array array = rs.getArray(columnName);
        if (array == null) return List.of();

        Object[] objects = (Object[]) array.getArray();
        return Arrays.stream(objects)
                .map(obj -> ((Number) obj).longValue())
                .toList();
    }

    public static Set<Long> getNullableLongSet(ResultSet rs, String columnName) throws SQLException {
        Array array = rs.getArray(columnName);
        if (array == null) return Set.of();

        Object[] objects = (Object[]) array.getArray();
        return Arrays.stream(objects)
                .map(obj -> ((Number) obj).longValue())
                .collect(Collectors.toSet());
    }


}
