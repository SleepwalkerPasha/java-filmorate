package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateFilm;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {

    private final FilmStorage filmStorage;

    private final FilmService filmService;

    private long count = 0;


    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getId() == null || filmStorage.getFilmById(film.getId()) == null) {
            validateFilm(film);
            film.setId(++count);
            log.info("Добавили новый фильм с id = '{}'", film.getId());
            return filmStorage.addFilm(film);
        } else {
            log.error("Данный фильм уже существует");
            throw new ValidationException("Данный фильм уже существует");
        }
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException, NotFoundException {
        if (film.getId() != null && filmStorage.getFilmById(film.getId()) != null) {
            validateFilm(film);
            log.info("Обновили фильм с id = '{}'", film.getId());
            return filmStorage.updateFilm(film);
        } else {
            log.error("Данного фильма нет. Добавьте");
            throw new NotFoundException("Данного фильма нет. Добавьте");
        }
    }

    @GetMapping
    public List<Film> getFilms() throws NotFoundException {
        List<Film> films = filmStorage.getFilms();
        if (CollectionUtils.isEmpty(films)) {
            log.info("список пустой");
            throw new NotFoundException("список пустой");
        }
        log.info("Получили список фильмов");
        return films;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            log.error("Фильма с id = {} не найдено", id);
            throw new NotFoundException("Фильма с id =" + id + "не найдено");
        }
        log.info("Вывод фильма с id = {}", id);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean addUserLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        return filmService.addUserLike(userId, filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean removeUserLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        return filmService.removeUserLike(userId, filmId);
    }

    @GetMapping("/popular")
    public Set<Film> getTopTenPopularFilmsByLikes(@RequestParam(defaultValue = "10", required = false) long count) {
        return filmService.getTopTenPopularFilmsByLikes(count);
    }
}
