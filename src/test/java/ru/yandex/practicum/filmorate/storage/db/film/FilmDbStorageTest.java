package ru.yandex.practicum.filmorate.storage.db.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        storage.addFilm(Film.builder()
                .id(1L)
                .name("First Movie")
                .description("Description")
                .releaseDate(LocalDate.of(2023, 1, 12))
                .duration(120)
                .rate(5)
                .mpa(new MpaRating(1L, "PG-13"))
                .genres(Set.of(new Genre(1L, "horror"), new Genre(2L, "Thriller")))
                .build());
    }

    @Test
    void addFilm() {
        Optional<Film> filmOptional = storage.addFilm(Film.builder()
                .id(2L)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2023, 1, 12))
                .duration(120)
                .rate(5)
                .mpa(new MpaRating(1L, "PG-13"))
                .genres(Set.of(new Genre(1L, "horror"), new Genre(2L, "Thriller")))
                .build());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", 2L));
    }

    @Test
    void updateFilm() {
        Optional<Film> filmOptional = storage.updateFilm(Film.builder()
                .id(1L)
                .name("Updated Name")
                .description("Description")
                .releaseDate(LocalDate.of(2023, 1, 12))
                .duration(120)
                .rate(5)
                .mpa(new MpaRating(1L, "PG-13"))
                .genres(Set.of(new Genre(1L, "horror"), new Genre(2L, "Thriller")))
                .build());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("name", "Updated Name"));
    }

    @Test
    void deleteFilm() {
        storage.deleteFilm(1L);

        Optional<Film> filmOptional = storage.getFilmById(1L);

        assertThat(filmOptional).isEmpty();
    }

    @Test
    void getFilmById() {
        Optional<Film> filmOptional = storage.getFilmById(1L);

        assertThat(filmOptional).isPresent().hasValueSatisfying(user -> assertThat(user)
                .hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    void getFilms() {
        List<Film> films = storage.getFilms();

        assertNotNull(films);
        assertEquals(films.size(), 1);
    }

    @Test
    void addUserLike() {
        Optional<User> user = userDbStorage.addUser(User.builder()
                .id(2L)
                .email("email@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        boolean shouldBeTrue = storage.addUserLike(user.get().getId(), 1L);
        assertTrue(shouldBeTrue);
    }

    @Test
    void removeUserLike() {
        Optional<User> user = userDbStorage.addUser(User.builder()
                .id(2L)
                .email("email@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        boolean shouldBeTrue = storage.addUserLike(user.get().getId(), 1L);

        boolean shouldBeTrueAfterDeletion = storage.removeUserLike(user.get().getId(), 1L);

        assertTrue(shouldBeTrueAfterDeletion);
    }

    @Test
    void getTopTenPopularFilmsByLikes() {
        Optional<User> user = userDbStorage.addUser(User.builder()
                .id(2L)
                .email("email@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        boolean shouldBeTrue = storage.addUserLike(user.get().getId(), 1L);

        List<Film> popularFilms = storage.getTopTenPopularFilmsByLikes(1L);

        assertNotNull(popularFilms);
        assertEquals(popularFilms.size(), 1);
    }
}