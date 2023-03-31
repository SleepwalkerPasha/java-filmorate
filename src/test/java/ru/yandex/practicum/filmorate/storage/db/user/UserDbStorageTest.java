package ru.yandex.practicum.filmorate.storage.db.user;

import lombok.RequiredArgsConstructor;
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


    @Test
    void addUser() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("mailmail@mail.ru")
                .login("login12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        assertThat(userOptional).isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 1L));

        userStorage.deleteUser(userOptional.get().getId());
    }

    @Test
    void updateUser() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("mailmail@mail.ru")
                .login("login12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        Optional<User> userOptional1 = userStorage.updateUser(User.builder()
                .id(userOptional.get().getId())
                .email("email@mail.ru")
                .login("login")
                .name("NewName")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        assertThat(userOptional1).isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", "NewName"));

        userStorage.deleteUser(userOptional.get().getId());
        userStorage.deleteUser(userOptional1.get().getId());
    }

    @Test
    void deleteUser() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("newmailmailmial@mail.ru")
                .login("lfsdfdfn12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        userStorage.deleteUser(userOptional.get().getId());

        Optional<User> userOptional1 = userStorage.getUserById(1L);

        assertThat(userOptional1).isEmpty();
    }

    @Test
    void getUserById() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("mailmailmailmiallama@mail.ru")
                .login("lgdfgdhhjj12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        Optional<User> userOptional1 = userStorage.getUserById(userOptional.get().getId());

        assertThat(userOptional).isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", userOptional.get().getId()));

        userStorage.deleteUser(userOptional1.get().getId());
    }

    @Test
    void getUsers() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("mailmaillamamammaaa@mail.ru")
                .login("login125678")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        Optional<User> userOptional1 = userStorage.addUser(User.builder()
                .email("madaddil@mail.ru")
                .login("login123333")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        List<User> users = userStorage.getUsers();

        assertNotNull(users);
        assertEquals(users.size(), 2);

        userStorage.deleteUser(userOptional.get().getId());
        userStorage.deleteUser(userOptional1.get().getId());
    }

    @Test
    void addFriend() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("mailmailgmailyandex@mail.ru")
                .login("login121116666")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        Optional<User> userOptional1 = userStorage.addUser(User.builder()
                .email("newEmail2446661@mail.ru")
                .login("login124447779")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        boolean shouldBeTrue = userStorage.addFriend(userOptional.get().getId(), userOptional1.get().getId());

        assertTrue(shouldBeTrue);

        userStorage.deleteUser(userOptional.get().getId());
        userStorage.deleteUser(userOptional1.get().getId());
    }

    @Test
    void deleteFriend() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("mailmail@mail.ru")
                .login("login12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        Optional<User> userOptional1 = userStorage.addUser(User.builder()
                .email("newEmail@mail.ru")
                .login("login12444")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        userStorage.addFriend(userOptional.get().getId(), userOptional1.get().getId());

        boolean shouldBeTrueAfterDeletion = userStorage.deleteFriend(userOptional.get().getId(), userOptional1.get().getId());

        assertTrue(shouldBeTrueAfterDeletion);

        userStorage.deleteUser(userOptional.get().getId());
        userStorage.deleteUser(userOptional1.get().getId());
    }

    @Test
    void getCommonFriends() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("mailmail@mail.ru")
                .login("login12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        Optional<User> userOptional1 = userStorage.addUser(User.builder()
                .email("newEmail@mail.ru")
                .login("login12444")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        Optional<User> userOptional2 = userStorage.addUser(User.builder()
                .email("sgdgsdg@mail.ru")
                .login("l12312344")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        userStorage.addFriend(userOptional.get().getId(), userOptional1.get().getId());
        userStorage.addFriend(userOptional.get().getId(), userOptional2.get().getId());
        userStorage.addFriend(userOptional1.get().getId(), userOptional2.get().getId());

        Set<User> commonFriends = userStorage.getCommonFriends(userOptional.get().getId(), userOptional1.get().getId());

        assertNotNull(commonFriends);
        assertEquals(1, commonFriends.size());
        assertEquals(commonFriends.toArray()[0], userOptional2.get());

        userStorage.deleteUser(userOptional.get().getId());
        userStorage.deleteUser(userOptional1.get().getId());
        userStorage.deleteUser(userOptional2.get().getId());
    }

    @Test
    void getFriends() {
        Optional<User> userOptional = userStorage.addUser(User.builder()
                .email("mailmail@mail.ru")
                .login("login12")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        Optional<User> userOptional1 = userStorage.addUser(User.builder()
                .email("newEmail@mail.ru")
                .login("login12444")
                .name("name")
                .birthday(LocalDate.of(2002, 2, 12))
                .build());

        boolean shouldBeTrue = userStorage.addFriend(userOptional.get().getId(), userOptional1.get().getId());

        Set<User> friends = userStorage.getFriends(userOptional.get().getId());

        Set<User> friendsOfSecond = userStorage.getFriends(userOptional1.get().getId());

        assertNotNull(friends);
        assertEquals(1, friends.size());

        assertNotNull(friends);
        assertEquals(0, friendsOfSecond.size());

        userStorage.deleteUser(userOptional.get().getId());
        userStorage.deleteUser(userOptional1.get().getId());
    }
}