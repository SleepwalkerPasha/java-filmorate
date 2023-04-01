package ru.yandex.practicum.filmorate.storage.db.film.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FilmGenresDto {

    @NotNull
    private final Long filmId;

    @NotNull
    private final Long genreId;
}
