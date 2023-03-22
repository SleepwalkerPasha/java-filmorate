package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public boolean addUserLike(long userId, long filmId) {
        userService.checkUser(userId);
        checkFilm(filmId);
        log.info("пролайкан фильм с id = {} у пользователя id = {}", filmId, userId);
        return filmStorage.getFilmById(filmId).addUserLike(userId);
    }

    public boolean removeUserLike(long userId, long filmId) {
        userService.checkUser(userId);
        checkFilm(filmId);
        log.info("убран лайн у фильма с id = {} у пользователя id = {}", filmId, userId);
        return filmStorage.getFilmById(filmId).removerUserLike(userId);
    }

    public List<Film> getTopTenPopularFilmsByLikes(long count) {
        log.info("получили топ {} фильмов по лайкам", count);
        return filmStorage.getFilms()
                .stream()
                .sorted(((o1, o2) -> Long.compare(o2.getUserLikesAmount(), o1.getUserLikesAmount())))
                .limit(count)
                .collect(Collectors.toList());
    }

    void checkFilm(Long filmId) {
        if (filmId == null || filmStorage.getFilmById(filmId) == null) {
            log.error("Фильма с id = {} не существует в хранилище", filmId);
            throw new NotFoundException("Фильма с id = " + filmId + "не существует в хранилище");
        }
    }
}
