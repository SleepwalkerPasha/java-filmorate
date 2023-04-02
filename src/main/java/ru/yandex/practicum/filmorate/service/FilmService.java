package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {


    private final FilmStorage filmStorage;

    private final UserService userService;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public boolean addUserLike(long userId, long filmId) {
        userService.checkUser(userId);
        checkFilm(filmId);
        log.info("пролайкан фильм с id = {} у пользователя id = {}", filmId, userId);
        return filmStorage.addUserLike(userId, filmId);
    }

    public boolean removeUserLike(long userId, long filmId) {
        userService.checkUser(userId);
        checkFilm(filmId);
        log.info("убран лайн у фильма с id = {} у пользователя id = {}", filmId, userId);
        return filmStorage.removeUserLike(userId, filmId);
    }

    public List<Film> getTopTenPopularFilmsByLikes(long count) {
        log.info("получили топ {} фильмов по лайкам", count);
        return filmStorage.getTopTenPopularFilmsByLikes(count);
    }

    void checkFilm(Long filmId) {
        if (filmId == null || filmStorage.getFilmById(filmId).isEmpty()) {
            log.error("Фильма с id = {} не существует в хранилище", filmId);
            throw new NotFoundException("Фильма с id = " + filmId + "не существует в хранилище");
        }
    }
}
