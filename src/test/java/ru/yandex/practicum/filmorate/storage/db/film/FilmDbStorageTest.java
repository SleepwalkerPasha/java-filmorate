package ru.yandex.practicum.filmorate.storage.db.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage storage;

    private final UserDbStorage userDbStorage;

    @Test
    void addFilm() {
        Film film1 = new Film(
                "Name",
                "Description",
                LocalDate.of(2023, 1, 12),
                new MpaRating(1L, "G"),
                5,
                120);
        film1.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        Optional<Film> filmOptional = storage.addFilm(film1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", filmOptional.get().getId()));

        storage.deleteFilm(filmOptional.get().getId());
    }

    @Test
    void updateFilm() {
        Film film1 = new Film(1L,
                "Name",
                "Description",
                LocalDate.of(2023, 1, 12),
                new MpaRating(1L, "G"),
                5,
                120);
        film1.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        Optional<Film> filmOptional1 = storage.addFilm(film1);

        Film film2 = new Film(filmOptional1.get().getId(),
                "Updated Movie",
                "Description",
                LocalDate.of(2023, 1, 12),
                new MpaRating(1L, "G"),
                5,
                120);
        film2.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));

        Optional<Film> filmOptional = storage.updateFilm(film2);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("name", "Updated Movie"));

        storage.deleteFilm(filmOptional.get().getId());
    }

    @Test
    void deleteFilm() {
        Film film1 = new Film(1L,
                "Name",
                "Description",
                LocalDate.of(2023, 1, 12),
                new MpaRating(1L, "G"),
                5,
                120);
        film1.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        Optional<Film> filmOptional1 = storage.addFilm(film1);

        storage.deleteFilm(filmOptional1.get().getId());

        Optional<Film> filmOptional = storage.getFilmById(filmOptional1.get().getId());

        assertThat(filmOptional).isEmpty();
    }

    @Test
    void getFilmById() {
        Film film1 = new Film(
                "Name",
                "Description",
                LocalDate.of(2023, 1, 12),
                new MpaRating(1L, "G"),
                5,
                120);
        film1.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        Optional<Film> filmOptional1 = storage.addFilm(film1);

        Optional<Film> filmOptional = storage.getFilmById(filmOptional1.get().getId());

        assertThat(filmOptional).isPresent().hasValueSatisfying(user -> assertThat(user)
                .hasFieldOrPropertyWithValue("id", filmOptional.get().getId()));

        storage.deleteFilm(filmOptional1.get().getId());
    }

    @Test
    void getFilms() {
        Film film1 = new Film(
                "Name",
                "Description",
                LocalDate.of(2023, 1, 12),
                new MpaRating(1L, "G"),
                5,
                120);
        film1.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        Optional<Film> filmOptional = storage.addFilm(film1);

        Film film2 = new Film(
                "Some name",
                "adadadadadad",
                LocalDate.of(2023, 1, 12),
                new MpaRating(1L, "G"),
                5,
                120);
        film2.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        Optional<Film> filmOptional1 = storage.addFilm(film2);


        List<Film> films = storage.getFilms();

        assertNotNull(films);
        assertEquals(films.size(), 2);

        storage.deleteFilm(filmOptional.get().getId());
        storage.deleteFilm(filmOptional1.get().getId());
    }

    @Test
    void addUserLike() {
        Film film1 = new Film(
                "Name",
                "Description",
                LocalDate.of(2023, 1, 12),
                new MpaRating(1L, "G"),
                5,
                120);
        film1.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        Optional<Film> filmOptional = storage.addFilm(film1);

        Optional<User> userOptional = userDbStorage.addUser(User.builder()
                .email("email@mail.ru")
                .name("name")
                .login("login")
                .birthday(LocalDate.of(2000, 2, 12))
                .build());

        boolean shouldBeTrue = storage.addUserLike(userOptional.get().getId(), filmOptional.get().getId());
        assertTrue(shouldBeTrue);

        storage.deleteFilm(filmOptional.get().getId());
        userDbStorage.deleteUser(userOptional.get().getId());
    }

    @Test
    void removeUserLike() {
        Film film1 = new Film(
                "Name",
                "Description",
                LocalDate.of(2023, 1, 12),
                new MpaRating(1L, "G"),
                5,
                120);
        film1.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        Optional<Film> filmOptional = storage.addFilm(film1);

        Optional<User> userOptional = userDbStorage.addUser(User.builder()
                .email("email@mail.ru")
                .name("name")
                .login("login")
                .birthday(LocalDate.of(2000, 2, 12))
                .build());


        boolean shouldBeTrue = storage.addUserLike(userOptional.get().getId(), filmOptional.get().getId());

        boolean shouldBeTrueAfterDeletion = storage.removeUserLike(userOptional.get().getId(), filmOptional.get().getId());

        assertTrue(shouldBeTrueAfterDeletion);

        storage.deleteFilm(1L);
        userDbStorage.deleteUser(userOptional.get().getId());
    }

    @Test
    void getTopTenPopularFilmsByLikes() {
        Film film1 = new Film(
                "Name",
                "Description",
                LocalDate.of(2023, 1, 12),
                new MpaRating(1L, "G"),
                5,
                120);
        film1.setGenres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        Optional<Film> filmOptional = storage.addFilm(film1);

        Optional<User> userOptional = userDbStorage.addUser(User.builder()
                .email("email@mail.ru")
                .name("name")
                .login("login")
                .birthday(LocalDate.of(2000, 2, 12))
                .build());


        boolean shouldBeTrue = storage.addUserLike(userOptional.get().getId(), filmOptional.get().getId());

        List<Film> popularFilms = storage.getTopTenPopularFilmsByLikes(1L);

        assertNotNull(popularFilms);
        assertEquals(popularFilms.size(), 1);

        storage.deleteFilm(filmOptional.get().getId());
        userDbStorage.deleteUser(userOptional.get().getId());
    }
}