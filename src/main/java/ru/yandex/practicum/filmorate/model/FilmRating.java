package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilmRating {

    private Long id;

    @NotBlank(message = "Название рейтинга не может быть пустым")
    private String title;
}
