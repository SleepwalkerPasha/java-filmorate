package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    private static int count = 0;

    private final Map<Integer, Film> filmMap = new HashMap<>();

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film, Errors errors) throws ValidationException {

        validateFilm(film);
        if (errors.hasErrors()) {
            log.error("ошибки в валидации {}", errors.getAllErrors());
            throw new ValidationException("ошибки в валидации" + errors.getAllErrors());
        }
        film.setId(++count);
        if (filmMap.get(film.getId()) == null) {
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
        validateFilm(film);
        if (filmMap.get(film.getId()) != null) {
            filmMap.put(film.getId(), film);
            log.info("Обновили фильм с id = '{}'", film.getId());
        } else {
            log.error("Данного фильма нет. Добавьте");
            throw new NotFoundException("Данного фильма нет. Добавьте");
        }
        // add exception
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() throws NotFoundException {
        List<Film> films = new ArrayList<>(filmMap.values());
        if (CollectionUtils.isEmpty(films)) {
            log.error("список пустой");
            throw new NotFoundException("список пустой");
        }
        log.info("Получили список фильмов");
        return films;
    }

    private void validateFilm(@Valid @NotNull Film film) throws ValidationException {
         if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза раньше нижней границы");
            throw new ValidationException("Дата релиза раньше нижней границы");
        }
    }
}
