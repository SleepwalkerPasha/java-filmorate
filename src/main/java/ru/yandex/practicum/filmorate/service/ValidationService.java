package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class ValidationService {

    public static void validateFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза раньше нижней границы");
            throw new ValidationException("Дата релиза раньше нижней границы");
        }
    }

    public static void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
    }
}
