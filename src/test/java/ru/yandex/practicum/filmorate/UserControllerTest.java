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
import org.springframework.http.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    private final HttpHeaders headers = new HttpHeaders();

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp() {
        headers.set("Content-Type", "application/json");
        userService.clearData();
    }

    static Stream<String> provideInvalidUserJsonCreate() {
        return Stream.of(
                """
                        {
                          "login": "doloreullamco",
                          "name": "test User",
                          "email": "mail.ru",
                          "birthday": "1980-08-20"
                        }
                        """, // Не верный email

                """
                        {
                          "login": "doloreullamco",
                          "name": "test User",
                          "email": "",
                          "birthday": "1980-08-20"
                        }
                        """, // пустой email

                """
                        {
                          "login": "dolore ullamco",
                          "name": "test User",
                          "email": "mail@mail.ru",
                          "birthday": "1980-08-20"
                        }
                        """, // логин с пробелами

                """
                        {
                          "login": "",
                          "name": "Nick Name",
                          "email": "mail@mail.ru",
                          "birthday": "1946-08-20"
                        }
                        """, // пустой логин

                """
                        {
                          "login": "dolore",
                          "name": "Nick Name",
                          "email": "mail@mail.ru",
                          "birthday": "2060-08-20"
                        }
                        """ // Дата рождения в будущем
        );
    }

    static Stream<String> provideInvalidUserJsonUpdate() {
        return Stream.of(
                """
                        {
                          "id": 1,
                          "login": "doloreullamco",
                          "name": "test User",
                          "email": "mail.ru",
                          "birthday": "1980-08-20"
                        }
                        """, // Не верный email

                """
                        {
                          "id": 1,
                          "login": "doloreullamco",
                          "name": "test User",
                          "email": "",
                          "birthday": "1980-08-20"
                        }
                        """, // пустой email

                """
                        {
                          "id": 1,
                          "login": "dolore ullamco",
                          "name": "test User",
                          "email": "mail@mail.ru",
                          "birthday": "1980-08-20"
                        }
                        """, // логин с пробелами

                """
                        {
                          "id": 1,
                          "login": "",
                          "name": "Nick Name",
                          "email": "mail@mail.ru",
                          "birthday": "1946-08-20"
                        }
                        """, // пустой логин

                """
                        {
                          "id": 1,
                          "login": "dolore",
                          "name": "Nick Name",
                          "email": "mail@mail.ru",
                          "birthday": "2060-08-20"
                        }
                        """ // Дата рождения в будущем
        );
    }

    static Stream<String> provideInvalidUserJsonIdUpdate() {
        return Stream.of(
                """
                        {
                          "login": "dolore",
                          "name": "Nick Name",
                          "email": "mail@mail.ru",
                          "birthday": "1946-08-20"
                        }
                        """, // Без Id

                """
                        {
                          "id": 123,
                          "login": "dolore",
                          "name": "Nick Name",
                          "email": "mail@mail.ru",
                          "birthday": "1946-08-20"
                        }
                        """ // Id отсутствует в списке
        );
    }

    @Test
    void getUsers() {
        userService.addUser(new User(1L, "test@mail.ru", "login1", "user1",
                LocalDate.of(1999, 2, 18)));
        userService.addUser(new User(2L, "test2@mail.ru", "login2", "user2",
                LocalDate.of(2001, 5, 25)));

        ResponseEntity<String> response = restTemplate.getForEntity("/users", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JsonElement expectedJson = JsonParser.parseString("""
                [
                    {
                        "id": 1,
                        "email": "test@mail.ru",
                        "login": "login1",
                        "name": "user1",
                        "birthday": "1999-02-18"
                    },
                    {
                        "id": 2,
                        "email": "test2@mail.ru",
                        "login": "login2",
                        "name": "user2",
                        "birthday": "2001-05-25"
                    }
                ]
                """);
        assertEquals(expectedJson, JsonParser.parseString(response.getBody()));
    }

    @Test
    void addUser() {
        String json = """
                {
                  "login": "testlogin",
                  "name": "Test User",
                  "email": "mail@mail.ru",
                  "birthday": "1946-08-20"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JsonElement expectedJson = JsonParser.parseString("""
                { "id": 1,
                  "login": "testlogin",
                  "name": "Test User",
                  "email": "mail@mail.ru",
                  "birthday": "1946-08-20"
                }
                """);
        assertEquals(expectedJson, JsonParser.parseString(response.getBody()));

        User createdUser = userService.getUsersMap().get(1L);
        assertNotNull(createdUser);
        assertEquals("testlogin", createdUser.getLogin());
        assertEquals("Test User", createdUser.getName());
        assertEquals("mail@mail.ru", createdUser.getEmail());
        assertEquals(LocalDate.of(1946, 8, 20), createdUser.getBirthday());
    }

    @Test
    void addUserWithoutName() {
        String json = """
                {
                  "login": "testlogin",
                  "email": "mail@mail.ru",
                  "birthday": "1946-08-20"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JsonElement expectedJson = JsonParser.parseString("""
                { "id": 1,
                  "login": "testlogin",
                  "name": "testlogin",
                  "email": "mail@mail.ru",
                  "birthday": "1946-08-20"
                }
                """);
        assertEquals(expectedJson, JsonParser.parseString(response.getBody()));

        User createdUser = userService.getUsersMap().get(1L);
        assertNotNull(createdUser);
        assertEquals("testlogin", createdUser.getLogin());
        assertEquals("testlogin", createdUser.getName());
        assertEquals("mail@mail.ru", createdUser.getEmail());
        assertEquals(LocalDate.of(1946, 8, 20), createdUser.getBirthday());
    }

    @Test
    void updateUser() {
        userService.addUser(new User(1L, "test@mail.ru", "login1", "user1",
                LocalDate.of(1999, 2, 18)));
        String json = """
                {
                  "id": 1,
                  "login": "login1Upd",
                  "name": "Test User upd",
                  "email": "mailupd@mail.ru",
                  "birthday": "1978-10-21"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        JsonElement expectedJson = JsonParser.parseString(json);
        assertEquals(expectedJson, JsonParser.parseString(response.getBody()));

        User updatedUser = userService.getUsersMap().get(1L);
        assertNotNull(updatedUser);
        assertEquals("login1Upd", updatedUser.getLogin());
        assertEquals("Test User upd", updatedUser.getName());
        assertEquals("mailupd@mail.ru", updatedUser.getEmail());
        assertEquals(LocalDate.of(1978, 10, 21), updatedUser.getBirthday());
    }

    @Test
    void updateUserWithoutName() {
        userService.addUser(new User(1L, "test@mail.ru", "login1", "user1",
                LocalDate.of(1999, 2, 18)));
        String json = """
                {
                  "id": 1,
                  "login": "login1Upd",
                  "email": "mailupd@mail.ru",
                  "birthday": "1978-10-21"
                }
                """;
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        User updatedUser = userService.getUsersMap().get(1L);
        assertNotNull(updatedUser);
        assertEquals("login1Upd", updatedUser.getLogin());
        assertEquals("login1Upd", updatedUser.getName());
        assertEquals("mailupd@mail.ru", updatedUser.getEmail());
        assertEquals(LocalDate.of(1978, 10, 21), updatedUser.getBirthday());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserJsonCreate")
    void addUserValidation(String json) {
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("\"error\":\"Bad Request\""));
    }

    @Test
    void addUserEmptyJson() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("\"error\":\"Bad Request\""));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserJsonUpdate")
    void updateUserValidation(String json) {
        userService.addUser(new User(1L, "test@mail.ru", "login1", "user1",
                LocalDate.of(1999, 2, 18)));
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("\"error\":\"Bad Request\""));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserJsonIdUpdate")
    void updateUserIdValidation(String json) {
        userService.addUser(new User(1L, "test@mail.ru", "login1", "user1",
                LocalDate.of(1999, 2, 18)));
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("\"error\":\"Internal Server Error\""));
    }

    @Test
    void updateUserEmptyJson() {
        userService.addUser(new User(1L, "test@mail.ru", "login1", "user1",
                LocalDate.of(1999, 2, 18)));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("\"error\":\"Bad Request\""));
    }

}
