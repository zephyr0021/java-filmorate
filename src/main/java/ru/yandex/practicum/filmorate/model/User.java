package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * User.
 */

@Data
@AllArgsConstructor
public class User {
    private Long id;

    @Email(message = "Электронная почта пользователя должна соответствовать формату электронного адреса")
    @NotBlank(message = "Электронная почта пользователя не может быть пустой")
    private String email;

    @NotBlank(message = "Логин пользователя не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин пользователя не может содержать пробелы")
    private String login;
    private String name;

    @PastOrPresent(message = "Дата рождения пользователя не может быть в будущем")
    private LocalDate birthday;
}
