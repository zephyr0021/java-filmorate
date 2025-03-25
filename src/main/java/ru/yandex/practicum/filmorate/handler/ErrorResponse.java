package ru.yandex.practicum.filmorate.handler;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorResponse {
    private final String error;
    private final String message;
}