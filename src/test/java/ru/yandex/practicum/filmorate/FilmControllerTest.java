package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmService filmService;

    @BeforeEach
    void setUp() {
        filmService.createFilm(new Film(1L, "test1", "test_descr1", LocalDate.of(1900, 12, 25), 10));
        filmService.createFilm(new Film(2L, "test2", "test_descr2", LocalDate.of(1900, 12, 25), 10));
    }

    @AfterEach
    void tearDown() {
        filmService.clearFilmsData();
    }

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
                        "}", // Отрицательная длительность

                "{\n" +
                        "  \"id\": 135,\n" +
                        "  \"name\": \"The Matrix\",\n" +
                        "  \"description\": \"Sci-fi action\",\n" +
                        "  \"releaseDate\": \"1900-12-25\",\n" +
                        "  \"duration\": -5\n" +
                        "}" // неизвестный ид
        );
    }

    @Test
    void getFilms() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("test2"));
    }

    @Test
    void getFilmById() throws Exception {
        mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test1"));
    }

    @Test
    void getUnknownFilmById() throws Exception {
        mockMvc.perform(get("/films/5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("not found"))
                .andExpect(jsonPath("$.message").value("Фильм с id  5 не найден"));
    }

    @Test
    void addFilms() throws Exception {
        String json = "{\n" +
                "  \"name\": \"The Matrix\",\n" +
                "  \"description\": \"Sci-fi action\",\n" +
                "  \"releaseDate\": \"1967-03-25\",\n" +
                "  \"duration\": 100\n" +
                "}";
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("The Matrix"));

        assertEquals(filmService.getFilmById(3L).getName(), "The Matrix");
    }

    @Test
    void updateFilms() throws Exception {
        String json = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"test1_upd\",\n" +
                "  \"description\": \"test_descr1_upd\",\n" +
                "  \"releaseDate\": \"1967-03-25\",\n" +
                "  \"duration\": 100\n" +
                "}";

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test1_upd"));

        assertEquals(filmService.getFilmById(1L).getName(), "test1_upd");
        assertEquals(filmService.getFilmById(1L).getDescription(), "test_descr1_upd");
        assertEquals(filmService.getFilmById(1L).getReleaseDate().toString(), "1967-03-25");
        assertEquals(filmService.getFilmById(1L).getDuration(), 100);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFilmJsonCreate")
    void addFilmValidation(String json) throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validation error"));
    }

    @Test
    void addFilmEmptyJson() throws Exception {
        String json = "{}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validation error"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFilmJsonUpdate")
    void updateFilmValidation(String json) throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validation error"));
    }

    @Test
    void updateFilmWithoutId() throws Exception {
        String json = "{\n" +
                "  \"name\": \"The Matrix\",\n" +
                "  \"description\": \"Sci-fi action\",\n" +
                "  \"releaseDate\": \"1900-12-25\",\n" +
                "  \"duration\": 10\n" +
                "}";

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validation error"));
    }

    @Test
    void updateFilmEmptyJson() throws Exception {
        String json = "{}";

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validation error"));
    }

}
