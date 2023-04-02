package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateFilm;

@Slf4j
@RestController
public class FilmController {

    private final FilmStorage filmStorage;

    private final FilmService filmService;

    private long count = 0;


    @Autowired
    public FilmController(@Qualifier("FilmDbStorage") FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getId() == null || filmStorage.getFilmById(film.getId()).isEmpty()) {
            validateFilm(film);
            film.setId(++count);
            log.info("Добавили новый фильм с id = '{}'", film.getId());
            filmStorage.addFilm(film);
            return film;
        } else {
            log.error("Данный фильм уже существует");
            throw new ValidationException("Данный фильм уже существует");
        }
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException, NotFoundException {
        if (film.getId() != null && filmStorage.getFilmById(film.getId()).isPresent()) {
            validateFilm(film);
            log.info("Обновили фильм с id = '{}'", film.getId());
            Optional<Film> result = filmStorage.updateFilm(film);
            return result.get();
        } else {
            log.error("Данного фильма нет. Добавьте");
            throw new NotFoundException("Данного фильма нет. Добавьте");
        }
    }

    @GetMapping("/films")
    public List<Film> getFilms() throws NotFoundException {
        List<Film> films = filmStorage.getFilms();
        log.info("Получили список фильмов");
        return films;
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable long id) {
        Optional<Film> film = filmStorage.getFilmById(id);
        if (film.isEmpty()) {
            log.error("Фильма с id = {} не найдено", id);
            throw new NotFoundException("Фильма с id =" + id + "не найдено");
        }
        log.info("Вывод фильма с id = {}", id);
        return film.get();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public boolean addUserLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        return filmService.addUserLike(userId, filmId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public boolean removeUserLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        return filmService.removeUserLike(userId, filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopTenPopularFilmsByLikes(@RequestParam(defaultValue = "10", required = false) long count) {
        return filmService.getTopTenPopularFilmsByLikes(count);
    }
}
