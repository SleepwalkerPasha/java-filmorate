package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    @Test
    public void testFindUserById() {

//		Optional<User> userOptional = userStorage.getUserById(1L);
//
//		assertThat(userOptional)
//				.isPresent()
//				.hasValueSatisfying(user ->
//						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
//				);
    }
}
