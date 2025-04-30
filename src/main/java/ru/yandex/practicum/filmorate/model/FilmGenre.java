package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilmGenre {
    private Long id;

    @NotBlank(message = "Название жанра не может быть пустым")
    private String name;

    public FilmGenre(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
