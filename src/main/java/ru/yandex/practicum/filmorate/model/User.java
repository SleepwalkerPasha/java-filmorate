package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
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

    private final Set<Long> friends = new HashSet<>();

    public boolean addFriend(long userId) {
        return friends.add(userId);
    }

    public boolean deleteFriend(long userId) {
        return friends.remove(userId);
    }

    public Set<Long> getFriends() {
        return new HashSet<>(friends);
    }
}
