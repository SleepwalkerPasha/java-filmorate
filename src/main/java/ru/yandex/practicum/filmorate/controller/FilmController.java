package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
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

    private static final int DESC_LIMIT = 200;

    private final Map<Integer, Film> filmMap = new HashMap<>();

    @PostMapping("/films")
    public Film addFilm(@NotNull @Valid @RequestBody Film film) throws ValidationException {
        validateFilm(film);
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
    public Film updateFilm(@NotNull @Valid @RequestBody Film film) throws ValidationException, NotFoundException {
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
        if (film.getName().isBlank()) {
            log.error("Название фильма пустое id = '{}'", film.getId());
            throw new ValidationException("Название фильма пустое");
        } else if (film.getDescription().length() > DESC_LIMIT) {
            log.error("Описание фильма превышает лимит");
            throw new ValidationException("Описание фильма превышает лимит");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза раньше нижней границы");
            throw new ValidationException("Дата релиза раньше нижней границы");
        } else if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма неположительна");
            throw new ValidationException("Продолжительность фильма неположительна");
        }
    }
}
