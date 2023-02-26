package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap;


    public InMemoryUserStorage() {
        userMap = new HashMap<>();
    }

    @Override
    public User addUser(User user) {
        return userMap.put(user.getId(), user);
    }

    @Override
    public User updateUser(User newUser) {
        return userMap.put(newUser.getId(), newUser);
    }

    @Override
    public void deleteUser(long userId) {
        userMap.remove(userId);
    }

    @Override
    public User getUserById(long userId) {
        return userMap.get(userId);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
    }
}
