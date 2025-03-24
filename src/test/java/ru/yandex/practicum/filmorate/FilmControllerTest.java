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
                "{\n" +
                        "  \"name\": \"\",\n" +
                        "  \"description\": \"Sci-fi action\",\n" +
                        "  \"releaseDate\": \"1900-12-25\",\n" +
                        "  \"duration\": 10\n" +
                        "}", // Пустое название

                "{\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"eVsHnlJqcle8LzLbMyajlPHJvq6xPAEBqvi0V55TAlZ6vokGPaTGgzI9ypdZv8X5X2StI10zqReOEy" +
                        "mkWTNcxCFbRqrmJBVrIdgQf9VDYXcE76DmMQ7jI4jzHZ0XKxx13dlujL9A3MbNudpQC4Uw1aoIsOoD" +
                        "cHS1JAHFXAEduHxtxNuo3Iq6tGj0xXZqDczZX2UqqTfmt\",\n" +
                        "  \"releaseDate\": \"1900-12-25\",\n" +
                        "  \"duration\": 10\n" +
                        "}", // Описание длиной 201 символ

                "{\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"NQ1qwgfJURRuOBe7rZJuRMNP4wCGFHKvrz5flsvTzz5NKXwP1II7Jytjjwn51G51eQBF2IfydfdRrkdq" +
                        "50DSPqWqOFri415GQb8HgwqI1ET0uF5wDFBS9iwzCysqqSTffd0BcRf1zHwPjMwDSOnQHETMtSEYTd" +
                        "V7PLRlZw4mgreyXaFpSjVXB4149KfTyeFL9YFXSANQbCly2IwtFAftdPKSTHrLzNFk2AEwInZGATHu" +
                        "q73LwzP5qmx89M\",\n" +
                        "  \"releaseDate\": \"1900-12-25\",\n" +
                        "  \"duration\": 10\n" +
                        "}", // Описание длиной 300 символов

                "{\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"Sci-fi action\",\n" +
                        "  \"releaseDate\": \"1800-12-25\",\n" +
                        "  \"duration\": 10\n" +
                        "}", // Дата раньше 1895

                "{\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"Sci-fi action\",\n" +
                        "  \"releaseDate\": \"1900-12-25\",\n" +
                        "  \"duration\": -5\n" +
                        "}" // Отрицательная длительность
        );
    }

    static Stream<String> provideInvalidFilmJsonUpdate() {
        return Stream.of(
                "{\n" +
                        "  \"id\": 1,\n" +
                        "  \"name\": \"\",\n" +
                        "  \"description\": \"Sci-fi action\",\n" +
                        "  \"releaseDate\": \"1900-12-25\",\n" +
                        "  \"duration\": 10\n" +
                        "}", // Пустое название

                "{\n" +
                        "  \"id\": 1,\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"eVsHnlJqcle8LzLbMyajlPHJvq6xPAEBqvi0V55TAlZ6vokGPaTGgzI9ypdZv8X5X2StI10zqReOEy" +
                        "mkWTNcxCFbRqrmJBVrIdgQf9VDYXcE76DmMQ7jI4jzHZ0XKxx13dlujL9A3MbNudpQC4Uw1aoIsOoD" +
                        "cHS1JAHFXAEduHxtxNuo3Iq6tGj0xXZqDczZX2UqqTfmt\",\n" +
                        "  \"releaseDate\": \"1900-12-25\",\n" +
                        "  \"duration\": 10\n" +
                        "}", // Описание длиной 201 символ

                "{\n" +
                        "  \"id\": 1,\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"NQ1qwgfJURRuOBe7rZJuRMNP4wCGFHKvrz5flsvTzz5NKXwP1II7Jytjjwn51G51eQBF2IfydfdRrkdq" +
                        "50DSPqWqOFri415GQb8HgwqI1ET0uF5wDFBS9iwzCysqqSTffd0BcRf1zHwPjMwDSOnQHETMtSEYTd" +
                        "V7PLRlZw4mgreyXaFpSjVXB4149KfTyeFL9YFXSANQbCly2IwtFAftdPKSTHrLzNFk2AEwInZGATHu" +
                        "q73LwzP5qmx89M\",\n" +
                        "  \"releaseDate\": \"1900-12-25\",\n" +
                        "  \"duration\": 10\n" +
                        "}", // Описание длиной 300 символов

                "{\n" +
                        "  \"id\": 1,\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"Sci-fi action\",\n" +
                        "  \"releaseDate\": \"1800-12-25\",\n" +
                        "  \"duration\": 10\n" +
                        "}", // Дата раньше 1895

                "{\n" +
                        "  \"id\": 1,\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"Sci-fi action\",\n" +
                        "  \"releaseDate\": \"1900-12-25\",\n" +
                        "  \"duration\": -5\n" +
                        "}" // Отрицательная длительность
        );
    }

    static Stream<String> provideInvalidFilmJsonIdUpdate() {
        return Stream.of(
                "{\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"Sci-fi action\",\n" +
                        "  \"releaseDate\": \"1900-12-25\",\n" +
                        "  \"duration\": 10\n" +
                        "}", // Без id

                "{\n" +
                        "  \"id\": 125,\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"Sci-fi action\",\n" +
                        "  \"releaseDate\": \"1900-12-25\",\n" +
                        "  \"duration\": 10\n" +
                        "}" // Id нет в списке
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
        JsonElement expectedJson = JsonParser.parseString(
                "[\n" +
                        "  {\n" +
                        "    \"id\": 1,\n" +
                        "    \"name\": \"The Matrix\",\n" +
                        "    \"description\": \"Sci-fi action\",\n" +
                        "    \"releaseDate\": \"1900-12-25\",\n" +
                        "    \"duration\": 10\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"id\": 2,\n" +
                        "    \"name\": \"The Dark Knight\",\n" +
                        "    \"description\": \"Superhero thriller\",\n" +
                        "    \"releaseDate\": \"1900-12-25\",\n" +
                        "    \"duration\": 10\n" +
                        "  }\n" +
                        "]"
        );
        assertEquals(expectedJson, JsonParser.parseString(response.getBody()));
    }

    @Test
    void addFilm() {
        String json = "{\n" +
                "  \"name\": \"The Matrix\",\n" +
                "  \"description\": \"Sci-fi action\",\n" +
                "  \"releaseDate\": \"1967-03-25\",\n" +
                "  \"duration\": 100\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        JsonElement expectedJson = JsonParser.parseString(
                "{\n" +
                        "  \"id\": 1,\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"Sci-fi action\",\n" +
                        "  \"releaseDate\": \"1967-03-25\",\n" +
                        "  \"duration\": 100\n" +
                        "}"
        );
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
        String json = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"The Matrix updated\",\n" +
                "  \"description\": \"Sci-fi action updated\",\n" +
                "  \"releaseDate\": \"1967-03-25\",\n" +
                "  \"duration\": 100\n" +
                "}";
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
