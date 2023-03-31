package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class MpaRating {

    @NotNull
    @Positive
    private final Long id;

    @NotBlank
    private final String name;
}
