package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Slf4j
public class ValidationService {

    private static final String REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);

    public static void validateFilm(Film film) throws ValidationException {
        if (film != null && film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(DATE)) {
                log.error("Дата релиза раньше нижней границы");
                throw new ValidationException("Дата релиза раньше нижней границы");
            } else if (film.getDescription().length() > 200) {
                log.error("Описание не больше 200 символов");
                throw new ValidationException("Описание не больше 200 символов");
            } else if (film.getDuration() <= 0) {
                log.error("Продолжительность фильма неположительна");
                throw new ValidationException("Продолжительность фильма неположительна");
            } else if (film.getName() == null || film.getName().isBlank()) {
                log.error("Название фильма пустое");
                throw new ValidationException("Название фильма пустое");
            }
        }
    }

    public static void validateUser(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.error("Логин пустой");
            throw new ValidationException("Логин пустой");
        } else if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Электронная почта пустая");
            throw new ValidationException("Электронная почта пустая");
        } else if (!Pattern
                .compile(REGEX_PATTERN)
                .matcher(user.getEmail())
                .matches()) {
            log.error("Электронная почта невалидна");
            throw new ValidationException("Электронная почта невалидна");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("День рождения должен быть в прошлом");
            throw new ValidationException("День рождения должен быть в прошлом");
        }
    }

    public static void setNameUser(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
    }
}
