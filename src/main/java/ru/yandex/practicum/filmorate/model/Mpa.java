package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mpa {

    private Long id;

    @NotBlank(message = "Название рейтинга не может быть пустым")
    private String name;

}
