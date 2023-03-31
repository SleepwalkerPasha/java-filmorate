package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        checkUser(userId);
        checkUser(friendId);
        userStorage.addFriend(userId, friendId);
        log.info("добавлен друг id = {} у пользователя в id = {}", friendId, userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        checkUser(userId);
        checkUser(friendId);
        userStorage.deleteFriend(userId, friendId);
        // delete friend
        log.info("удален друг id = {} у пользователя в id = {}", friendId, userId);

    }

    public Set<User> getCommonFriends(Long userId1, Long userId2) {
        checkUser(userId1);
        checkUser(userId2);
        log.info("Вывод общих друзей");
        return userStorage.getCommonFriends(userId1, userId2);
    }

    public Set<User> getFriends(Long userId) {
        checkUser(userId);
        return userStorage.getFriends(userId);
    }

    void checkUser(Long userId1) {
        if (userId1 == null || userStorage.getUserById(userId1).isEmpty()) {
            log.error("Пользователя с id = {} не существует в хранилище", userId1);
            throw new NotFoundException("Пользователя с id = " + userId1 + " не существует в хранилище");
        }
    }
}
