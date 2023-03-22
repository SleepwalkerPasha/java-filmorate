package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractInMemoryStorage;

import java.util.List;

@Component
public class InMemoryUserStorage extends AbstractInMemoryStorage<User> implements UserStorage {


    public InMemoryUserStorage() {
        super();
    }

    @Override
    public User addUser(User user) {
        return put(user.getId(), user);
    }

    @Override
    public User updateUser(User newUser) {
        return put(newUser.getId(), newUser);
    }

    @Override
    public void deleteUser(long userId) {
        remove(userId);
    }

    @Override
    public User getUserById(long userId) {
        return getById(userId);
    }

    @Override
    public List<User> getUsers() {
        return getValues();
    }
}
