package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film> implements FilmStorage {


    public InMemoryFilmStorage() {
        super();
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        return  Optional.of(getById(id));
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        return Optional.of(put(film.getId(), film));
    }

    @Override
    public Optional<Film> updateFilm(Film newFilm) {
        return Optional.of(put(newFilm.getId(), newFilm));
    }

    @Override
    public void deleteFilm(long filmId) {
        remove(filmId);
    }

    @Override
    public List<Film> getFilms() {
        return getValues();
    }

    @Override
    public boolean addUserLike(Long userId, Long filmId) {
        return getFilmById(filmId).get().addUserLike(userId);
    }

    @Override
    public boolean removeUserLike(Long userId, Long filmId) {
        return getFilmById(filmId).get().removeUserLike(userId);
    }

    @Override
    public List<Film> getTopTenPopularFilmsByLikes(Long count) {
        return getFilms()
                .stream()
                .sorted(((o1, o2) -> Long.compare(o2.getUserLikesAmount(), o1.getUserLikesAmount())))
                .limit(count)
                .collect(Collectors.toList());
    }
}
