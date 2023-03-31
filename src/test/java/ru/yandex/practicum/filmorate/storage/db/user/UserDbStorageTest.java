package ru.yandex.practicum.filmorate.storage.db.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @BeforeEach
    void setUp() {
        userStorage.addUser(User.builder()
                .email("email@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());
    }

    @AfterEach
    void tearDown() {
        userStorage.deleteUser(1L);
    }

    @Test
    void addUser() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("mailmail@mail.ru")
                .login("login12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        assertThat(userOptional).isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 2L));

    }

    @Test
    void updateUser() {
        Optional<User> userOptional = userStorage.updateUser(User.builder()
                .id(1L)
                .email("email@mail.ru")
                .login("login")
                .name("NewName")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        assertThat(userOptional).isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", "NewName"));
    }

    @Test
    void deleteUser() {
        userStorage.deleteUser(1L);

        Optional<User> userOptional = userStorage.getUserById(1L);

        assertThat(userOptional).isEmpty();
    }

    @Test
    void getUserById() {
        Optional<User> userOptional = userStorage.getUserById(1L);

        assertThat(userOptional).isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    void getUsers() {
        List<User> users = userStorage.getUsers();

        assertNotNull(users);
        assertEquals(users.size(), 1);
    }

    @Test
    void addFriend() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("newEmail@mail.ru")
                .login("login12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        boolean shouldBeTrue = userStorage.addFriend(1L, userOptional.get().getId());

        assertTrue(shouldBeTrue);
    }

    @Test
    void deleteFriend() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("newEmail@mail.ru")
                .login("login12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        boolean shouldBeTrue = userStorage.addFriend(1L, userOptional.get().getId());

        boolean shouldBeTrueAfterDeletion = userStorage.deleteFriend(1L, userOptional.get().getId());

        assertTrue(shouldBeTrueAfterDeletion);
    }

    @Test
    void getCommonFriends() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("newEmail@mail.ru")
                .login("login12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        Optional<User> userOptional1 = userStorage.addUser(User.builder()
                .email("someemail@mail.ru")
                .login("login123")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        userStorage.addFriend(1L, userOptional.get().getId());
        userStorage.addFriend(1L, userOptional1.get().getId());
        userStorage.addFriend(userOptional.get().getId(), userOptional1.get().getId());

        Set<User> commonFriends = userStorage.getCommonFriends(1L, userOptional.get().getId());

        assertNotNull(commonFriends);
        assertEquals(1, commonFriends.size());
        assertEquals(commonFriends.toArray()[0], userOptional1.get());
    }

    @Test
    void getFriends() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("newEmail@mail.ru")
                .login("login12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        boolean shouldBeTrue = userStorage.addFriend(1L, userOptional.get().getId());

        Set<User> friends = userStorage.getFriends(1L);

        Set<User> friendsOfSecond = userStorage.getFriends(userOptional.get().getId());

        assertNotNull(friends);
        assertEquals(1, friends.size());

        assertNotNull(friends);
        assertEquals(0, friendsOfSecond.size());
    }
}