package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mpa {

    private Long id;

    @NotBlank(message = "Название рейтинга не может быть пустым")
    private String name;

    public Mpa(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
