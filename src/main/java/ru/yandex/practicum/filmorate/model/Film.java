package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Film {

    private Long id;

    @NotBlank(message = "Название фильма пустое")
    @NotNull
    private final String name;

    @Size(max = 200, message = "Описание не больше 200 символов")
    private final String description;
    private final LocalDate releaseDate;

    @NotNull
    @Positive(message = "Продолжительность фильма неположительна")
    private final Integer duration;

    private final Set<Long> userLikes = new HashSet<>();

    public boolean addUserLike(Long userId) {
        return userLikes.add(userId);
    }

    public boolean removerUserLike(Long userId) {
        return userLikes.remove(userId);
    }

    public long getUserLikesAmount() {
        return userLikes.size();
    }
}
