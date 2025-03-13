package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */

@Data
@AllArgsConstructor
public class Film {
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание фильма должно быть не длиннее 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;

    @AssertTrue(message = "Дата релиза фильма должна быть не раньше 28 декабря 1895 года")
    @JsonIgnore
    public boolean isReleaseDateValid() {
        if (releaseDate == null) {
            return true;
        }
        return releaseDate.isAfter(LocalDate.of(1895, 12, 28));
    }
}
