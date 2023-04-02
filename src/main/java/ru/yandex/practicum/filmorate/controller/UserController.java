package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.yandex.practicum.filmorate.service.ValidationService.setNameUser;
import static ru.yandex.practicum.filmorate.service.ValidationService.validateUser;

@Slf4j
@RestController
public class UserController {

    private final UserStorage userStorage;

    private final UserService userService;

    private long count = 0;

    @Autowired
    public UserController(@Qualifier("UserDbStorage") UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        if (user.getId() == null || userStorage.getUserById(user.getId()).isEmpty()) {
            validateUser(user);
            user.setId(++count);
            setNameUser(user);
            log.info("Добавили нового пользователя с id '{}'", user.getId());
            userStorage.addUser(user);
            return user;
        } else {
            log.error("Данный пользователь уже существует");
            throw new ValidationException("Данный пользователь уже существует");
        }
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws NotFoundException, ValidationException {
        if (user.getId() != null && userStorage.getUserById(user.getId()).isPresent()) {
            validateUser(user);
            setNameUser(user);
            log.info("Обновили пользователя с id '{}'", user.getId());
            userStorage.updateUser(user);
            return user;
        } else {
            log.error("Данного пользователя нет. Зарегистрируйтесь");
            throw new NotFoundException("Данного пользователя нет. Зарегистрируйтесь");
        }
    }

    @GetMapping("/users")
    public List<User> getUsers() throws NotFoundException {
        List<User> users = userStorage.getUsers();
        if (CollectionUtils.isEmpty(users)) {
            log.error("список пустой");
            throw new NotFoundException("список пустой");
        }
        log.info("получили список пользователей");

        return users;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable long id) {
        Optional<User> user = userStorage.getUserById(id);
        if (user.isEmpty()) {
            log.error("Данного пользователя с id {} не существует", id);
            throw new NotFoundException("Данного пользователя с id " + id + " не существует");
        }
        log.info("вывод пользователя с id {}", id);
        return user.get();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFriend(friendId, id);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Set<User> getFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
