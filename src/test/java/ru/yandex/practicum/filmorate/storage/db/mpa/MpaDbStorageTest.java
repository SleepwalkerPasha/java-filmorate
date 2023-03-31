package ru.yandex.practicum.filmorate.storage.db.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {


    private final MpaDbStorage storage;


    @Test
    void getRatingMPAById() {
        Optional<MpaRating> optionalMpaRating = storage.getRatingMPAById(1L);

        assertThat(optionalMpaRating)
                .isPresent()
                .hasValueSatisfying(mpa -> assertThat(mpa).hasFieldOrPropertyWithValue("name", "G"));
    }

    @Test
    void getRatingMPAList() {
        List<MpaRating> mpaList = storage.getRatingMPAList();

        assertNotNull(mpaList);
        assertEquals(5, mpaList.size());
    }
}