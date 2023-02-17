package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateFilm;

@Slf4j
@RestController
public class FilmController {

    private int count = 0;

    private final Map<Integer, Film> filmMap = new HashMap<>();

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getId() == null || filmMap.get(film.getId()) == null) {
            film.setId(++count);
            validateFilm(film);
            filmMap.put(film.getId(), film);
            log.info("Добавили новый фильм с id = '{}'", film.getId());
        } else {
            log.error("Данный фильм уже существует");
            throw new ValidationException("Данный фильм уже существует");
        }
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException, NotFoundException {
        if (film.getId() != null && filmMap.get(film.getId()) != null) {
            validateFilm(film);
            filmMap.put(film.getId(), film);
            log.info("Обновили фильм с id = '{}'", film.getId());
        } else {
            log.error("Данного фильма нет. Добавьте");
            throw new NotFoundException("Данного фильма нет. Добавьте");
        }
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() throws NotFoundException {
        List<Film> films = new ArrayList<>(filmMap.values());
        if (CollectionUtils.isEmpty(films)) {
            log.info("список пустой");
            throw new NotFoundException("список пустой");
        }
        log.info("Получили список фильмов");
        return films;
    }
}
