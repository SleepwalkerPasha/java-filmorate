package ru.yandex.practicum.filmorate.storage.db.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Qualifier("UserMapper")
    private final RowMapper<User> mapper;

    @Autowired
    public UserDbStorage(JdbcTemplate template, RowMapper<User> mapper) {
        this.jdbcTemplate = template;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> addUser(User user) {
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int count = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.of(user.getBirthday(), LocalTime.NOON)));
            return ps;
        }, keyHolder);
        long keyValue = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(keyValue);
        if (count == 0) {
            log.info("пользователь c данным id уже существует");
            return Optional.empty();
        }
        log.info("добавлен пользователь с id {}", user.getId());
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User newUser) {
        String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ?" +
                "WHERE ID = ?";
        int count = jdbcTemplate.update(sql, newUser.getEmail(), newUser.getLogin(), newUser.getName(),
                newUser.getBirthday(), newUser.getId());
        if (count == 0) {
            log.info("пользователя с таким id нет");
            return Optional.empty();
        }
        log.info("обновлен пользователь с id {}", newUser.getId());
        return Optional.of(newUser);
    }

    @Override
    public void deleteUser(long userId) {
        deleteUserFromMovieLikes(userId);
        deleteUserFromFriendship(userId);
        String sql = "DELETE FROM USERS WHERE ID = ?";
        int count = jdbcTemplate.update(sql, userId);
        if (count == 0)
            log.info("нет пользователя с данным id");
        else
            log.info("удален пользователь с данным id");
    }

    private void deleteUserFromMovieLikes(long userId) {
        String sql = "DELETE FROM MOVIELIKES WHERE USER_ID = ?";
        int count = jdbcTemplate.update(sql, userId);
        if (count == 0)
            log.info("нет пользователя с данным id в MovieLikes");
        else
            log.info("удален пользователь с данным id из MovieLikes");
    }

    private void deleteUserFromFriendship(long userId) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USERID = ? OR FRIENDID = ?";
        int count = jdbcTemplate.update(sql, userId, userId);
        if (count == 0)
            log.info("нет пользователя с данным id в FRIENDSHIP");
        else
            log.info("удален пользователь с данным id из FRIENDSHIP");
    }

    @Override
    public Optional<User> getUserById(long userId) {
        String sql = "SELECT * FROM USERS AS U WHERE U.id = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sql, mapper, userId);
            if (user != null) {
                log.info("найден пользователь с id = {}", userId);
                return Optional.of(user);
            } else {
                log.info("пользователь с id = {} не найден", userId);
                return Optional.empty();
            }
        } catch (EmptyResultDataAccessException e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM USERS LIMIT ?";
        log.info("выведен список 100 пользователей");
        return jdbcTemplate.query(sql, mapper, 100);
    }

    @Override
    public boolean addFriend(Long userId, Long friendId) {
        String sql = "INSERT INTO FRIENDSHIP (USERID, FRIENDID, STATUS) VALUES (?,?,?)";
        int count = jdbcTemplate.update(sql, userId, friendId, FriendshipStatus.UNCONFIRMED.toString());
        if (count == 0) {
            log.info("произошла ошибка при добавлении значения в таблицу");
            return false;
        } else
            log.info("пользователь {} отправил запрос на дружбу {}", userId, friendId);
        return true;
    }

    @Override
    public boolean deleteFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USERID = ? AND FRIENDID = ?";
        int count = jdbcTemplate.update(sql, userId, friendId);
        if (count == 0) {
            log.info("произошла ошибка при удалении значения из таблицы");
            return false;
        } else
            log.info("пользователь {} удалил из друзей {}", userId, friendId);
        return true;
    }

    @Override
    public Set<User> getCommonFriends(Long userId, Long friendId) {
        String sql = "SELECT * FROM USERS WHERE ID IN " +
                "(SELECT FRIENDID FROM FRIENDSHIP WHERE USERID = ? " +
                "INTERSECT " +
                "SELECT FRIENDID FROM FRIENDSHIP WHERE USERID = ?)";
        log.info("список общих друзей");
        return new HashSet<>(jdbcTemplate.query(sql, mapper, userId, friendId));
    }

    @Override
    public Set<User> getFriends(Long userId) {
        String sql = "SELECT * FROM USERS WHERE ID IN (SELECT FRIENDID FROM FRIENDSHIP WHERE USERID = ?)";
        log.info("список друзей");
        return new HashSet<>(jdbcTemplate.query(sql, mapper, userId));
    }

}
