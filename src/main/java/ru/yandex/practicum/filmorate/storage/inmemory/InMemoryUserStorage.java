package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage extends AbstractInMemoryStorage<User> implements UserStorage {


    public InMemoryUserStorage() {
        super();
    }

    @Override
    public Optional<User> addUser(User user) {
        return Optional.of(put(user.getId(), user));
    }

    @Override
    public Optional<User> updateUser(User newUser) {
        return Optional.of(put(newUser.getId(), newUser));
    }

    @Override
    public void deleteUser(long userId) {
        remove(userId);
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return Optional.of(getById(userId));
    }

    @Override
    public List<User> getUsers() {
        return getValues();
    }

    @Override
    public boolean addFriend(Long userId, Long friendId) {
        Optional<User> userOptional = getUserById(userId);
        if (userOptional.isPresent()) {
            userOptional.get().addFriend(friendId, FriendshipStatus.UNCONFIRMED);
            return true;
        } else
            return false;
    }

    @Override
    public boolean deleteFriend(Long userId, Long friendId) {
        Optional<User> userOptional = getUserById(userId);
        if (userOptional.isPresent()) {
            userOptional.get().deleteFriend(friendId);
            return true;
        } else
            return false;
    }

    @Override
    public Set<User> getCommonFriends(Long userId, Long friendId) {
        Set<User> commonFriends = new HashSet<>(Set.copyOf(getFriends(userId)));
        commonFriends.retainAll(getFriends(friendId));
        return commonFriends;
    }

    @Override
    public Set<User> getFriends(Long userId) {
        Set<User> friends = new HashSet<>();
        for (Long id : getUserById(userId).get().getFriends()) {
            friends.add(getUserById(id).get());
        }
        return friends;
    }
}
