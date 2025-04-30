package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Component
@Primary
@Profile("test")
public class InMemoryMpaStorage implements MpaStorage {

    private final Map<Long, Mpa> mpas = Map.of(
            1L, new Mpa(1L, "PG"),
            2L, new Mpa(2L, "R-17")
            );

    public Collection<Mpa> getAllRatings() {
        return mpas.values();
    }

    public Optional<Mpa> getMpa(Long id) {
        return mpas.values().stream()
                .filter(mpa -> mpa.getId().equals(id))
                .findFirst();
    }

}
