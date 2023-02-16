package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


class ValidationServiceTest {

    @Test
    void validateFilmSuccess() {
        Assertions.assertDoesNotThrow(() -> ValidationService.validateFilm(new Film(1, "sfdfsf", "adfsdfs",
                LocalDate.of(2002, 12, 12), 120)));
    }

    @Test
    void validateFilmThrowsException() {
        Assertions.assertThrows(ValidationException.class, () -> ValidationService.validateFilm(new Film(1, "sfdfsf", "adfsdfs",
                LocalDate.of(1660, 12, 12), 120)));
    }

    @Test
    void validateUser() {
        User user = new User(1, "mail@ru", "login", "",
                LocalDate.of(1984, 12, 12));
        ValidationService.validateUser(user);
        Assertions.assertEquals("login", user.getName());
    }
}