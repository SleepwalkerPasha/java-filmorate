package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateFilm;
import static ru.yandex.practicum.filmorate.service.ValidationService.validateUser;
import static ru.yandex.practicum.filmorate.service.ValidationService.setNameUser;


class ValidationServiceTest {

    @Test
    void validateFilmSuccess() {
        Assertions.assertDoesNotThrow(() -> validateFilm(new Film(1, "sfdfsf", "adfsdfs",
                LocalDate.of(2002, 12, 12), 120)));
    }

    @Test
    void validateFilmFailName() {
        Film film = new Film(1, "", "asdfsf",
                LocalDate.of(1920, 12, 12), 120);
        Assertions.assertThrows(ValidationException.class, () -> validateFilm(film), "Название фильма пустое");

        film.setName(null);

        Assertions.assertThrows(ValidationException.class, () -> validateFilm(film), "Название фильма пустое");
    }

    @Test
    void validateFilmFailDescription() {
        Film film = new Film(1, "dadada", "Пятеро друзей ( комик-группа «Шарло»), " +
                "приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
                " а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.",
                LocalDate.of(1920, 12, 12), 120);

        Assertions.assertThrows(ValidationException.class, () -> validateFilm(film), "Описание не больше 200 символов");
    }

    @Test
    void validateFilmFailDuration() {
        Film film = new Film(1, "sdfsdfsfs", "asdfsf",
                LocalDate.of(1920, 12, 12), -1);

        Assertions.assertThrows(ValidationException.class, () -> validateFilm(film), "Продолжительность фильма неположительна");
    }

    @Test
    void validateFilmFailReleaseDate() {
        Film film = new Film(1, "sdfsdfsfs", "asdfsf",
                LocalDate.of(1800, 12, 12), -1);

        Assertions.assertThrows(ValidationException.class, () -> validateFilm(film), "Дата релиза раньше нижней границы");
    }

    @Test
    void validateUserSuccess() {
        User user = new User(1, "mail@mail.ru", "login", "name",
                LocalDate.of(2002,9,29));
        Assertions.assertDoesNotThrow(() -> validateUser(user));
    }

    @Test
    void validateUserFailLogin() {
        User user = new User(1, "mail@mail.ru", "", "name",
                LocalDate.of(2002,9,29));
        Assertions.assertThrows(ValidationException.class, () -> validateUser(user), "Логин пустой");

        user.setLogin(null);

        Assertions.assertThrows(ValidationException.class, () -> validateUser(user), "Логин пустой");
    }

    @Test
    void validateUserFailEmail() {
        User user = new User(1, "adfafaf", "", "name",
                LocalDate.of(2002,9,29));
        Assertions.assertThrows(ValidationException.class, () -> validateUser(user), "Электронная почта невалидна");

        user.setEmail(null);

        Assertions.assertThrows(ValidationException.class, () -> validateUser(user), "Электронная почта пустая");

        user.setEmail("");

        Assertions.assertThrows(ValidationException.class, () -> validateUser(user), "Электронная почта пустая");
    }

    @Test
    void validateUserFailBirthday() {
        User user = new User(1, "adfafaf", "", "name",
                LocalDate.of(2222,9,29));
        Assertions.assertThrows(ValidationException.class, () -> validateUser(user), "День рождения должен быть в прошлом");
    }

    @Test
    void setUserName() {
        User user = new User(1, "mail@ru", "login", "",
                LocalDate.of(1984, 12, 12));
        setNameUser(user);
        Assertions.assertEquals("login", user.getName());
    }
}