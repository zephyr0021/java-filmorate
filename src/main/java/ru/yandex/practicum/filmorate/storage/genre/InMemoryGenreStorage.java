package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Component
@Primary
@Profile("test")
public class InMemoryGenreStorage implements GenreStorage {

    private final Map<Long, FilmGenre> genres = Map.of(
            1L, new FilmGenre(1L, "Horror"),
            2L, new FilmGenre(2L, "Comedy")
    );

    public Optional<FilmGenre> getGenre(Long id) {
        return genres.values().stream()
                .filter(genre -> genre.getId().equals(id))
                .findFirst();
    }

    public Collection<FilmGenre> getGenres() {
        return genres.values();
    }
}
