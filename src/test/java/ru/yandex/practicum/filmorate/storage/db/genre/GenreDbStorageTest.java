package ru.yandex.practicum.filmorate.storage.db.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private final GenreDbStorage storage;

    @BeforeEach
    void setUp() {
        String sql = "INSERT INTO GENRE (ID, NAME) VALUES (1, 'horror')";
        jdbcTemplate.update(sql);
    }

    @Test
    void getGenreById() {
        Optional<Genre> genreOptional = storage.getGenreById(1L);

        assertThat(genreOptional)
                .isPresent().hasValueSatisfying(genre -> assertThat(genre)
                        .hasFieldOrPropertyWithValue("name", "horror"));
    }

    @Test
    void getGenres() {
        List<Genre> genres = storage.getGenres();

        assertNotNull(genres);
        assertEquals(1,genres.size());
    }
}