package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.ValidationService.setNameUser;
import static ru.yandex.practicum.filmorate.service.ValidationService.validateUser;

@Slf4j
@RestController
public class UserController {

    private final Map<Integer, User> userMap = new HashMap<>();

    private int count = 0;

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        if (user.getId() == null || userMap.get(user.getId()) == null) {
            user.setId(++count);
            userMap.put(user.getId(), user);
            setNameUser(user);
            validateUser(user);
            log.info("Добавили нового пользователя с id '{}'", user.getId());
        } else {
            log.error("Данный пользователь уже существует");
            throw new ValidationException("Данный пользователь уже существует");
        }
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws NotFoundException, ValidationException {
        if (user.getId() != null && userMap.get(user.getId()) != null) {
            userMap.put(user.getId(), user);
            setNameUser(user);
            validateUser(user);
            log.info("Обновили пользователя с id '{}'", user.getId());
        } else {
            log.error("Данного пользователя нет. Зарегистрируйтесь");
            throw new NotFoundException("Данного пользователя нет. Зарегистрируйтесь");
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() throws NotFoundException {
        List<User> users = new ArrayList<>(userMap.values());
        if (CollectionUtils.isEmpty(users)) {
            log.error("список пустой");
            throw new NotFoundException("список пустой");
        }
        log.info("получили список пользователей");

        return users;
    }
}
