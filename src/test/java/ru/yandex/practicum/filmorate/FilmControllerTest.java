package ru.yandex.practicum.filmorate;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FilmService filmService;

    private final HttpHeaders headers = new HttpHeaders();

    static Stream<String> provideInvalidFilmJsonCreate() {
        return Stream.of(
                """
                        {
                            "name": "",
                            "description": "Sci-fi action",
                            "releaseDate": "1900-12-25",
                            "duration": 10
                        }
                        """, // Пустое название
                """
                        {
                            "name": "The Matrix",
                            "description": "eVsHnlJqcle8LzLbMyajlPHJvq6xPAEBqvi0V55TAlZ6vokGPaTGgzI9ypdZv8X5X2StI10zqReOEy"
                                           "mkWTNcxCFbRqrmJBVrIdgQf9VDYXcE76DmMQ7jI4jzHZ0XKxx13dlujL9A3MbNudpQC4Uw1aoIsOoD"
                                           "cHS1JAHFXAEduHxtxNuo3Iq6tGj0xXZqDczZX2UqqTfm",
                            "releaseDate": "1900-12-25",
                            "duration": 10
                        }
                        """, // Описание длиной 201 символ
                """
                        {
                            "name": "The Matrix",
                            "description": "NQ1qwgfJURRuOBe7rZJuRMNP4wCGFHKvrz5flsvTzz5NKXwP1II7Jytjjwn51G51eQBF2IfydfdRrkdq"
                                           "50DSPqWqOFri415GQb8HgwqI1ET0uF5wDFBS9iwzCysqqSTffd0BcRf1zHwPjMwDSOnQHETMtSEYTd"
                                           "V7PLRlZw4mgreyXaFpSjVXB4149KfTyeFL9YFXSANQbCly2IwtFAftdPKSTHrLzNFk2AEwInZGATHu"
                                           "q73LwzP5qmx89M",
                            "releaseDate": "1900-12-25",
                            "duration": 10
                        }
                        """, // Описание длиной 300 символов
                """
                        {
                            "name": "The Matrix",
                            "description": "Sci-fi action",
                            "releaseDate": "1800-12-25",
                            "duration": 10
                        }
                        """, // Дата раньше 1895
                """
                        {
                            "name": "The Matrix",
                            "description": "Sci-fi action",
                            "releaseDate": "1900-12-25",
                            "duration": -5
                        }
                        """ // Отрицательная длительность
        );
    }

    static Stream<String> provideInvalidFilmJsonUpdate() {
        return Stream.of(
                """
                        {
                            "id": 1,
                            "name": "",
                            "description": "Sci-fi action",
                            "releaseDate": "1900-12-25",
                            "duration": 10
                        }
                        """, // Пустое название
                """
                        {
                            "id": 1,
                            "name": "The Matrix",
                            "description": "eVsHnlJqcle8LzLbMyajlPHJvq6xPAEBqvi0V55TAlZ6vokGPaTGgzI9ypdZv8X5X2StI10zqReOEy"
                                           "mkWTNcxCFbRqrmJBVrIdgQf9VDYXcE76DmMQ7jI4jzHZ0XKxx13dlujL9A3MbNudpQC4Uw1aoIsOoD"
                                           "cHS1JAHFXAEduHxtxNuo3Iq6tGj0xXZqDczZX2UqqTfm",
                            "releaseDate": "1900-12-25",
                            "duration": 10
                        }
                        """, // Описание длиной 201 символ
                """
                        {
                            "id": 1,
                            "name": "The Matrix",
                            "description": "NQ1qwgfJURRuOBe7rZJuRMNP4wCGFHKvrz5flsvTzz5NKXwP1II7Jytjjwn51G51eQBF2IfydfdRrkdq"
                                           "50DSPqWqOFri415GQb8HgwqI1ET0uF5wDFBS9iwzCysqqSTffd0BcRf1zHwPjMwDSOnQHETMtSEYTd"
                                           "V7PLRlZw4mgreyXaFpSjVXB4149KfTyeFL9YFXSANQbCly2IwtFAftdPKSTHrLzNFk2AEwInZGATHu"
                                           "q73LwzP5qmx89M",
                            "releaseDate": "1900-12-25",
                            "duration": 10
                        }
                        """, // Описание длиной 300 символов
                """
                        {
                            "id": 1,
                            "name": "The Matrix",
                            "description": "Sci-fi action",
                            "releaseDate": "1800-12-25",
                            "duration": 10
                        }
                        """, // Дата раньше 1895
                """
                        {
                            "id": 1,
                            "name": "The Matrix",
                            "description": "Sci-fi action",
                            "releaseDate": "1900-12-25",
                            "duration": -5
                        }
                        """ // Отрицательная длительность
        );
    }

    static Stream<String> provideInvalidFilmJsonIdUpdate() {
        return Stream.of(
                """
                        {
                            "name": "The Matrix",
                            "description": "Sci-fi action",
                            "releaseDate": "1900-12-25",
                            "duration": 10
                        }
                        """, // Без id
                """
                        {
                            "id": 125,
                            "name": "The Matrix",
                            "description": "Sci-fi action",
                            "releaseDate": "1900-12-25",
                            "duration": 10
                        }
                        """ // Id нет в списке
        );
    }

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp() {
        headers.set("Content-Type", "application/json");
        filmService.clearData();
    }

    @Test
    void getFilms() {
        filmService.addFilm(new Film(1L, "The Matrix", "Sci-fi action",
                LocalDate.of(1900, 12, 25), 10));
        filmService.addFilm(new Film(2L, "The Dark Knight", "Superhero thriller",
                LocalDate.of(1900, 12, 25), 10));
        ResponseEntity<String> response = restTemplate.getForEntity("/films", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JsonElement expectedJson = JsonParser.parseString("""
                [
                    {
                        "id": 1,
                        "name": "The Matrix",
                        "description": "Sci-fi action",
                        "releaseDate": "1900-12-25",
                        "duration": 10
                    },
                    {
                        "id": 2,
                        "name": "The Dark Knight",
                        "description": "Superhero thriller",
                        "releaseDate": "1900-12-25",
                        "duration": 10
                    }
                ]
                """);
        assertEquals(expectedJson, JsonParser.parseString(response.getBody()));
    }

    @Test
    void addFilm() {
        String json = """
                {
                  "name": "The Matrix",
                  "description": "Sci-fi action",
                  "releaseDate": "1967-03-25",
                  "duration": 100
                }""";
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        JsonElement expectedJson = JsonParser.parseString("""
                { "id": 1,
                  "name": "The Matrix",
                  "description": "Sci-fi action",
                  "releaseDate": "1967-03-25",
                  "duration": 100
                }""");
        Film createdFilm = filmService.getFilmsMap().get(1L);
        assertNotNull(createdFilm);
        assertEquals(expectedJson, JsonParser.parseString(response.getBody()));
        assertEquals(1L, createdFilm.getId());
        assertEquals("The Matrix", createdFilm.getName());
        assertEquals("Sci-fi action", createdFilm.getDescription());
        assertEquals(LocalDate.of(1967, 3, 25), createdFilm.getReleaseDate());
        assertEquals(100, createdFilm.getDuration());
    }

    @Test
    void updateFilm() {
        filmService.addFilm(new Film(1L, "The Matrix", "Sci-fi action",
                LocalDate.of(1900, 12, 25), 10));
        String json = """
                {
                  "id": 1,
                  "name": "The Matrix updated",
                  "description": "Sci-fi action updated",
                  "releaseDate": "1967-03-25",
                  "duration": 100
                }""";
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JsonElement expectedJson = JsonParser.parseString(json);
        assertEquals(expectedJson, JsonParser.parseString(response.getBody()));
        Film updatedFilm = filmService.getFilmsMap().get(1L);
        assertNotNull(updatedFilm);
        assertEquals(1L, updatedFilm.getId());
        assertEquals("The Matrix updated", updatedFilm.getName());
        assertEquals("Sci-fi action updated", updatedFilm.getDescription());
        assertEquals(LocalDate.of(1967, 3, 25), updatedFilm.getReleaseDate());
        assertEquals(100, updatedFilm.getDuration());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFilmJsonCreate")
    void addFilmValidation(String json) {
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("\"error\":\"Bad Request\""));
    }

    @Test
    void addFilmEmptyJson() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("\"error\":\"Bad Request\""));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFilmJsonUpdate")
    void updateFilmValidation(String json) {
        filmService.addFilm(new Film(1L, "The Matrix", "Sci-fi action",
                LocalDate.of(1900, 12, 25), 10));
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("\"error\":\"Bad Request\""));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFilmJsonIdUpdate")
    void updateFilmIdValidation(String json) {
        filmService.addFilm(new Film(1L, "The Matrix", "Sci-fi action",
                LocalDate.of(1900, 12, 25), 10));
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("\"error\":\"Internal Server Error\""));
    }

    @Test
    void updateFilmEmptyJson() {
        filmService.addFilm(new Film(1L, "The Matrix", "Sci-fi action",
                LocalDate.of(1900, 12, 25), 10));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("\"error\":\"Bad Request\""));
    }
}
