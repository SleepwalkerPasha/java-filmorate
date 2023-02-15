package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private final Map<Integer, User> userMap = new HashMap<>();

    private static int count = 0;

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user, Errors errors) throws ValidationException {
        validateUser(user);
        if (errors.hasErrors()) {
            log.error("ошибки в валидации {}", errors.getAllErrors());
            throw new ValidationException("ошибки в валидации" + errors.getAllErrors());
        }
        user.setId(++count);
        if (userMap.get(user.getId()) == null) {
            userMap.put(user.getId(), user);
            log.info("Добавили нового пользователя с id '{}'", user.getId());
        } else {
            log.error("Данный пользователь уже существует");
            throw new ValidationException("Данный пользователь уже существует");
        }
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws NotFoundException, ValidationException {
        validateUser(user);
        if (userMap.get(user.getId()) != null) {
            userMap.put(user.getId(), user);
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

    private void validateUser(@Valid User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
