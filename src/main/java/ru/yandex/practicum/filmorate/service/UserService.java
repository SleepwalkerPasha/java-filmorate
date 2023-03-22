package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        checkUser(userId);
        checkUser(friendId);
        log.info("добавлен друг id = {} у пользователя в id = {}", friendId, userId);
        userStorage.getUserById(userId).addFriend(friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        checkUser(userId);
        checkUser(friendId);
        log.info("удален друг id = {} у пользователя в id = {}", friendId, userId);
        userStorage.getUserById(userId).deleteFriend(friendId);
    }

    public Set<User> getCommonFriends(Long userId1, Long userId2) {
        checkUser(userId1);
        checkUser(userId2);
        Set<Long> commonFriendsIds = new HashSet<>(Set.copyOf(userStorage.getUserById(userId1).getFriends()));
        commonFriendsIds.retainAll(userStorage.getUserById(userId2).getFriends());
        Set<User> commonFriends = new HashSet<>();
        for (Long id : commonFriendsIds) {
            commonFriends.add(userStorage.getUserById(id));
        }
        log.info("Вывод общих друзей");
        return commonFriends;
    }

    public List<User> getFriends(Long userId) {
        checkUser(userId);
        List<User> friendsList = new ArrayList<>();
        for (Long id : userStorage.getUserById(userId).getFriends()) {
            friendsList.add(userStorage.getUserById(id));
        }
        return friendsList;
    }

    void checkUser(Long userId1) {
        if (userId1 == null || userStorage.getUserById(userId1) == null) {
            log.error("Пользователя с id = {} не существует в хранилище", userId1);
            throw new NotFoundException("Пользователя с id = " + userId1 + " не существует в хранилище");
        }
    }
}
