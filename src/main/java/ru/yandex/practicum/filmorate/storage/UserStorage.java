package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    Optional<User> addUser(User user);

    Optional<User> updateUser(User newUser);

    void deleteUser(long userId);

    Optional<User> getUserById(long userId);

    List<User> getUsers();

    boolean addFriend(Long userId, Long friendId);

    boolean deleteFriend(Long userId, Long friendId);

    Set<User> getCommonFriends(Long userId, Long friendId);

    Set<User> getFriends(Long userId);
}
