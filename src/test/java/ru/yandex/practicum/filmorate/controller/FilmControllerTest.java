package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        filmController.addFilm(new Film(1, "ssfsf", "asdfsf",
                LocalDate.of(1920, 12, 12), 120));
    }

    @Test
    void addFilmSuccess() {
        Film film = new Film(null, "ssfsf", "asdfsf",
                LocalDate.of(1920, 12, 12), 120);
        Assertions.assertDoesNotThrow(() -> filmController.addFilm(film));
    }

    @Test
    void addFilmAlreadyExist() {
        Film film = new Film(1, "ssfsf", "asdfsf",
                LocalDate.of(1920, 12, 12), 120);
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film), "Данный фильм уже существует");
    }

    @Test
    void updateFilmSuccess() {
        Film film = new Film(1, "ssfsf", "sdfsdfsdfsd",
                LocalDate.of(1920, 12, 12), 120);
        Assertions.assertDoesNotThrow(() -> filmController.updateFilm(film));
    }

    @Test
    void updateFilmNotExist() {
        Film film = new Film(999, "ssfsf", "asdfsf",
                LocalDate.of(1920, 12, 12), 120);
        Assertions.assertThrows(NotFoundException.class, () -> filmController.updateFilm(film), "Данного фильма нет. Добавьте");
    }

    @Test
    void getFilmsSuccess() {
        List<Film> films = filmController.getFilms();
        Assertions.assertDoesNotThrow(() -> filmController.getFilms());
        Assertions.assertNotNull(films);
        Assertions.assertEquals(1, films.size());
    }

    @Test
    void getFilmsFail() {
        filmController = new FilmController();
        Assertions.assertThrows(NotFoundException.class, () -> filmController.getFilms(), "список пустой");
    }

}