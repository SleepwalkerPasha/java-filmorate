package ru.yandex.practicum.filmorate.storage.db.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private final MpaDbStorage storage;

    @BeforeEach
    void setUp() {
        String sql = "INSERT INTO RATINGMPA (ID,NAME) VALUES ( 1, 'PG-13' )";
        jdbcTemplate.update(sql);
    }

    @Test
    void getRatingMPAById() {
        Optional<MpaRating> optionalMpaRating = storage.getRatingMPAById(1L);

        assertThat(optionalMpaRating)
                .isPresent()
                .hasValueSatisfying(mpa -> assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG-13"));
    }

    @Test
    void getRatingMPAList() {
        List<MpaRating> mpaList = storage.getRatingMPAList();

        assertNotNull(mpaList);
        assertEquals(1, mpaList.size());
    }
}