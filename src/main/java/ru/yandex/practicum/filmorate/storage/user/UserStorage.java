package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User newUser);

    void deleteUser(long userId);

    User getUserById(long userId);

    List<User> getUsers();
}