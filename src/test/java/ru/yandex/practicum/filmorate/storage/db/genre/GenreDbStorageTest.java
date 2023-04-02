package ru.yandex.practicum.filmorate.storage.db.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {

    private final GenreDbStorage storage;

    @Test
    void getGenreById() {
        Optional<Genre> genreOptional = storage.getGenreById(1L);

        assertThat(genreOptional)
                .isPresent().hasValueSatisfying(genre -> assertThat(genre)
                        .hasFieldOrPropertyWithValue("name", "Комедия"));
    }

    @Test
    void getGenres() {
        List<Genre> genres = storage.getGenres();

        assertNotNull(genres);
        assertEquals(6, genres.size());
    }
}