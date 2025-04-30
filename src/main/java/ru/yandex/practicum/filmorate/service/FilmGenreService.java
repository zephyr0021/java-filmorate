package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmGenreService {
    private final GenreStorage genreStorage;

    public Collection<FilmGenre> getAllGenres() {
        return genreStorage.getGenres();
    }

    public FilmGenre getGenreById(Long id) {
        return genreStorage.getGenre(id).orElseThrow(() -> new NotFoundException("Жанр с id " + id + " не найден"));
    }
}
