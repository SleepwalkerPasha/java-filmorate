package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

class UserControllerTest {

    UserController userController;

    @BeforeEach
    void setUp() {
        UserStorage storage = new InMemoryUserStorage();
        userController = new UserController(storage, new UserService(storage));
        userController.addUser(new User(1L,"mail@mail.ru", "login", "name",
                LocalDate.of(2002,9,29)));
    }

    @Test
    void addUserSuccess() {
        User user = new User(null, "mail@mail.ru", "login", "name",
                LocalDate.of(2002,9,29));
        Assertions.assertDoesNotThrow(() -> userController.addUser(user));
    }

    @Test
    void addUserAlreadyExist() {
        User user = new User(1L, "mail@mail.ru", "login", "name",
                LocalDate.of(2002,9,29));
        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user), "Данный пользователь уже существует");
    }

    @Test
    void updateUserSuccess() {
        User user = new User(1L, "mail@mail.ru", "login", "name",
                LocalDate.of(2002,9,29));
        Assertions.assertDoesNotThrow(() -> userController.updateUser(user));
    }

    @Test
    void updateUserNotExist() {
        User user = new User(999L, "mail@mail.ru", "login", "name",
                LocalDate.of(2002,9,29));
        Assertions.assertThrows(NotFoundException.class, () -> userController.updateUser(user), "Данного пользователя нет. Зарегистрируйтесь");
    }

    @Test
    void getUsersSuccess() {
        List<User> users = userController.getUsers();
        Assertions.assertDoesNotThrow(() -> userController.getUsers());
        Assertions.assertNotNull(users);
        Assertions.assertEquals(1, users.size());
    }

    @Test
    void getUsersFail() {
        UserStorage storage = new InMemoryUserStorage();
        userController = new UserController(storage, new UserService(storage));
        Assertions.assertThrows(NotFoundException.class, () -> userController.getUsers(), "список пустой");
    }
}