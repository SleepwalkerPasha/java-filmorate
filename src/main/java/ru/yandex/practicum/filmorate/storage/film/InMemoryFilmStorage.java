package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.AbstractInMemoryStorage;

import java.util.List;

@Component
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film> implements FilmStorage {


    public InMemoryFilmStorage() {
        super();
    }

    @Override
    public Film getFilmById(long id) {
        return getById(id);
    }

    @Override
    public Film addFilm(Film film) {
        return put(film.getId(), film);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        return put(newFilm.getId(), newFilm);
    }

    @Override
    public void deleteFilm(long filmId) {
        remove(filmId);
    }

    @Override
    public List<Film> getFilms() {
        return getValues();
    }
}
