package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film newFilm);

    void deleteFilm(long filmId);

    Film getFilmById(long id);

    List<Film> getFilms();
}
