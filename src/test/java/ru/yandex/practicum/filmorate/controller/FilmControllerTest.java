package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void setUp() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage, new UserService(new InMemoryUserStorage()));
        filmController = new FilmController(filmStorage, filmService);
        filmController.addFilm(new Film(1L, "ssfsf", "asdfsf",
                LocalDate.of(1920, 12, 12), new MpaRating(1L, "name"), 5, 120));
    }

    @Test
    void addFilmSuccess() {
        Film film = new Film(null, "ssfsf", "asdfsf",
                LocalDate.of(1920, 12, 12), new MpaRating(1L, "name"), 5, 120);
        Assertions.assertDoesNotThrow(() -> filmController.addFilm(film));
    }

    @Test
    void addFilmAlreadyExist() {
        Film film = new Film(1L, "ssfsf", "asdfsf",
                LocalDate.of(1920, 12, 12), new MpaRating(1L, "name"), 5, 120);
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film), "Данный фильм уже существует");
    }

    @Test
    void updateFilmSuccess() {
        Film film = new Film(1L, "ssfsf", "sdfsdfsdfsd",
                LocalDate.of(1920, 12, 12), new MpaRating(1L, "name"), 5, 120);
        Assertions.assertDoesNotThrow(() -> filmController.updateFilm(film));
    }

    @Test
    void updateFilmNotExist() {
        Film film = new Film(999L, "ssfsf", "asdfsf",
                LocalDate.of(1920, 12, 12), new MpaRating(1L, "name"), 5, 120);
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
    void getFilmsEmpty() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage, new UserService(new InMemoryUserStorage()));
        filmController = new FilmController(filmStorage, filmService);
        Assertions.assertEquals(0, filmController.getFilms().size());
    }

}