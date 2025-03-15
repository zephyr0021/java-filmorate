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
                "{\n" +
                        "  \"login\": \"doloreullamco\",\n" +
                        "  \"name\": \"test User\",\n" +
                        "  \"email\": \"mail.ru\",\n" +
                        "  \"birthday\": \"1980-08-20\"\n" +
                        "}", // Не верный email

                "{\n" +
                        "  \"login\": \"doloreullamco\",\n" +
                        "  \"name\": \"test User\",\n" +
                        "  \"email\": \"\",\n" +
                        "  \"birthday\": \"1980-08-20\"\n" +
                        "}", // пустой email

                "{\n" +
                        "  \"login\": \"dolore ullamco\",\n" +
                        "  \"name\": \"test User\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1980-08-20\"\n" +
                        "}", // логин с пробелами

                "{\n" +
                        "  \"login\": \"\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}", // пустой логин

                "{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"2060-08-20\"\n" +
                        "}" // Дата рождения в будущем
        );
    }

    static Stream<String> provideInvalidUserJsonUpdate() {
        return Stream.of(
                "{\n" +
                        "  \"id\": 1,\n" +
                        "  \"login\": \"doloreullamco\",\n" +
                        "  \"name\": \"test User\",\n" +
                        "  \"email\": \"mail.ru\",\n" +
                        "  \"birthday\": \"1980-08-20\"\n" +
                        "}", // Не верный email

                "{\n" +
                        "  \"id\": 1,\n" +
                        "  \"login\": \"doloreullamco\",\n" +
                        "  \"name\": \"test User\",\n" +
                        "  \"email\": \"\",\n" +
                        "  \"birthday\": \"1980-08-20\"\n" +
                        "}", // пустой email

                "{\n" +
                        "  \"id\": 1,\n" +
                        "  \"login\": \"dolore ullamco\",\n" +
                        "  \"name\": \"test User\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1980-08-20\"\n" +
                        "}", // логин с пробелами

                "{\n" +
                        "  \"id\": 1,\n" +
                        "  \"login\": \"\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}", // пустой логин

                "{\n" +
                        "  \"id\": 1,\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"2060-08-20\"\n" +
                        "}" // Дата рождения в будущем
        );
    }

    static Stream<String> provideInvalidUserJsonIdUpdate() {
        return Stream.of(
                "{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}", // Без Id

                "{\n" +
                        "  \"id\": 123,\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}" // Id отсутствует в списке
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
        JsonElement expectedJson = JsonParser.parseString(
                "[\n" +
                        "  {\n" +
                        "    \"id\": 1,\n" +
                        "    \"email\": \"test@mail.ru\",\n" +
                        "    \"login\": \"login1\",\n" +
                        "    \"name\": \"user1\",\n" +
                        "    \"birthday\": \"1999-02-18\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"id\": 2,\n" +
                        "    \"email\": \"test2@mail.ru\",\n" +
                        "    \"login\": \"login2\",\n" +
                        "    \"name\": \"user2\",\n" +
                        "    \"birthday\": \"2001-05-25\"\n" +
                        "  }\n" +
                        "]"
        );
        assertEquals(expectedJson, JsonParser.parseString(response.getBody()));
    }

    @Test
    void addUser() {
        String json = "{\n" +
                "  \"login\": \"testlogin\",\n" +
                "  \"name\": \"Test User\",\n" +
                "  \"email\": \"mail@mail.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        JsonElement expectedJson = JsonParser.parseString(
                "{ \"id\": 1,\n" +
                        "  \"login\": \"testlogin\",\n" +
                        "  \"name\": \"Test User\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}"
        );
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
        String json = "{\n" +
                "  \"login\": \"testlogin\",\n" +
                "  \"email\": \"mail@mail.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        JsonElement expectedJson = JsonParser.parseString(
                "{ \"id\": 1,\n" +
                        "  \"login\": \"testlogin\",\n" +
                        "  \"name\": \"testlogin\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}"
        );
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
        String json = "{\n" +
                "  \"id\": 1,\n" +
                "  \"login\": \"login1Upd\",\n" +
                "  \"name\": \"Test User upd\",\n" +
                "  \"email\": \"mailupd@mail.ru\",\n" +
                "  \"birthday\": \"1978-10-21\"\n" +
                "}";
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
        String json = "{\n" +
                "  \"id\": 1,\n" +
                "  \"login\": \"login1Upd\",\n" +
                "  \"email\": \"mailupd@mail.ru\",\n" +
                "  \"birthday\": \"1978-10-21\"\n" +
                "}";
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
