package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateFilm;
import static ru.yandex.practicum.filmorate.service.ValidationService.validateUser;
import static ru.yandex.practicum.filmorate.service.ValidationService.setNameUser;


class ValidationServiceTest {

    @Test
    void validateFilmSuccess() {
        Assertions.assertDoesNotThrow(() -> validateFilm(new Film(1L, "sfdfsf", "adfsdfs",
                LocalDate.of(2002, 12, 12), Genre.ACTION, RatingMpa.G,120)));
    }

    @Test
    void validateFilmFailName() {
        Film film = new Film(1L, "", "asdfsf",
                LocalDate.of(1920, 12, 12), Genre.ACTION, RatingMpa.G,120);
        Assertions.assertThrows(ValidationException.class, () -> validateFilm(film), "Название фильма пустое");

        film.setName(null);

        Assertions.assertThrows(ValidationException.class, () -> validateFilm(film), "Название фильма пустое");
    }

    @Test
    void validateFilmFailDescription() {
        Film film = new Film(1L, "dadada", "Пятеро друзей ( комик-группа «Шарло»), " +
                "приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
                " а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.",
                LocalDate.of(1920, 12, 12), Genre.ACTION, RatingMpa.G,120);

        Assertions.assertThrows(ValidationException.class, () -> validateFilm(film), "Описание не больше 200 символов");
    }

    @Test
    void validateFilmFailDuration() {
        Film film = new Film(1L, "sdfsdfsfs", "asdfsf",
                LocalDate.of(1920, 12, 12), Genre.ACTION, RatingMpa.G,-1);

        Assertions.assertThrows(ValidationException.class, () -> validateFilm(film), "Продолжительность фильма неположительна");
    }

    @Test
    void validateFilmFailReleaseDate() {
        Film film = new Film(1L, "sdfsdfsfs", "asdfsf",
                LocalDate.of(1800, 12, 12), Genre.ACTION, RatingMpa.G,-1);

        Assertions.assertThrows(ValidationException.class, () -> validateFilm(film), "Дата релиза раньше нижней границы");
    }

    @Test
    void validateUserSuccess() {
        User user = new User(1L, "mail@mail.ru", "login", "name",
                LocalDate.of(2002,9,29));
        Assertions.assertDoesNotThrow(() -> validateUser(user));
    }

    @Test
    void validateUserFailLogin() {
        User user = new User(1L, "mail@mail.ru", "", "name",
                LocalDate.of(2002,9,29));
        User user1 = new User(1L, "mail@mail.ru", null, "name",
                LocalDate.of(2002,9,29));

        Assertions.assertThrows(ValidationException.class, () -> validateUser(user), "Логин пустой");
        Assertions.assertThrows(ValidationException.class, () -> validateUser(user1), "Логин пустой");
    }

    @Test
    void validateUserFailEmail() {
        User user = new User(1L, "adfafaf", "", "name",
                LocalDate.of(2002,9,29));
        User user1 = new User(1L, null, "", "name",
                LocalDate.of(2002,9,29));

        User user2 = new User(1L, "", "", "name",
                LocalDate.of(2002,9,29));

        Assertions.assertThrows(ValidationException.class, () -> validateUser(user), "Электронная почта невалидна");
        Assertions.assertThrows(ValidationException.class, () -> validateUser(user1), "Электронная почта пустая");
        Assertions.assertThrows(ValidationException.class, () -> validateUser(user2), "Электронная почта пустая");
    }

    @Test
    void validateUserFailBirthday() {
        User user = new User(1L, "adfafaf", "", "name",
                LocalDate.of(2222,9,29));
        Assertions.assertThrows(ValidationException.class, () -> validateUser(user), "День рождения должен быть в прошлом");
    }

    @Test
    void setUserName() {
        User user = new User(1L, "mail@ru", "login", "",
                LocalDate.of(1984, 12, 12));
        setNameUser(user);
        Assertions.assertEquals("login", user.getName());
    }
}