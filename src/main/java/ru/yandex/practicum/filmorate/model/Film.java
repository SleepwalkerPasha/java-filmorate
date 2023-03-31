package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Film {

    private Long id;

    @NotBlank(message = "Название фильма пустое")
    @NotNull
    private final String name;

    @Size(max = 200, message = "Описание не больше 200 символов")
    private final String description;

    @NotNull
    private final LocalDate releaseDate;

    private Set<Genre> genres;

    @NotNull
    @JsonProperty("mpa")
    private MpaRating mpa;

    @Positive
    private Integer rate;

    @NotNull
    @Positive(message = "Продолжительность фильма неположительна")
    private final Integer duration;

    private Set<Long> userLikes;

    public Film(@NotBlank @JsonProperty("name") String name, @Size(max = 200) @JsonProperty("description") String description, @JsonProperty("releaseDate") LocalDate releaseDate, @JsonProperty("duration") Integer duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = new HashSet<>();
        this.userLikes = new HashSet<>();
    }

    public Film(@NotBlank String name, @Size(max = 200) String description, LocalDate releaseDate, MpaRating mpa, Integer rate, Integer duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.mpa = mpa;
        this.duration = duration;
        genres = new HashSet<>();
        userLikes = new HashSet<>();
        this.rate = rate;
        id = null;
    }

    public Film(@Positive Long id, @NotBlank String name, @Size(max = 200) String description, LocalDate releaseDate, MpaRating mpa, Integer rate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.mpa = mpa;
        this.rate = rate;
        this.duration = duration;
        this.genres = new HashSet<>();
        this.userLikes = new HashSet<>();
    }

    public Film(@Positive Long id, @NotBlank String name, @Size(max = 200) String description, LocalDate releaseDate, Integer rate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.mpa = null;
        this.rate = rate;
        this.duration = duration;
        this.genres = new HashSet<>();
        this.userLikes = new HashSet<>();
    }

    public boolean addUserLike(Long userId) {
        return userLikes.add(userId);
    }

    public boolean removeUserLike(Long userId) {
        return userLikes.remove(userId);
    }

    public long getUserLikesAmount() {
        return userLikes.size();
    }
}
