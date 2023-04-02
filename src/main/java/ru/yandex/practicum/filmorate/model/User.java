package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {

    private Long id;

    @Email(message = "Электронная почта невалидна")
    @NotNull
    @NotBlank(message = "Электронная почта пустая")
    private final String email;

    @NotBlank
    @NotNull
    @Pattern(regexp = "\\S+")
    private final String login;

    private String name;

    @Past(message = "День рождения должен быть в прошлом")
    private final LocalDate birthday;

    private final Map<Long, FriendshipStatus> friends = new HashMap<>();

    public void addFriend(long userId, FriendshipStatus status) {
        friends.put(userId, status);
    }

    public void deleteFriend(long userId) {
        friends.remove(userId);
    }

    public Set<Long> getFriends() {
        return new HashSet<>(friends.keySet());
    }
}
