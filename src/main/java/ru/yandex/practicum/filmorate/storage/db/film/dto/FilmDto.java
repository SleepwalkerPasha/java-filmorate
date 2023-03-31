package ru.yandex.practicum.filmorate.storage.db.film.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
public class FilmDto {

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
    private Integer mpa;

    @Positive
    private Integer rate;

    @NotNull
    @Positive(message = "Продолжительность фильма неположительна")
    private final Integer duration;

}
