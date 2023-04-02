package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> addFilm(Film film);

    Optional<Film> updateFilm(Film newFilm);

    void deleteFilm(long filmId);

    Optional<Film> getFilmById(long id);

    List<Film> getFilms();

    boolean addUserLike(Long userId, Long filmId);

    boolean removeUserLike(Long userId, Long filmId);

    List<Film> getTopTenPopularFilmsByLikes(Long count);
}
