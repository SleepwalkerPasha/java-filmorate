package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmMap;

    public InMemoryFilmStorage() {
        filmMap = new HashMap<>();
    }

    @Override
    public Film getFilmById(long id) {
        return filmMap.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        return filmMap.put(film.getId(), film);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        return filmMap.put(newFilm.getId(), newFilm);
    }

    @Override
    public void deleteFilm(long filmId) {
        filmMap.remove(filmId);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmMap.values());
    }
}
