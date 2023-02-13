package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class Film {

    private Integer id;

    @NotNull
    @NotBlank
    private String name;

    @Length
    private String description;

    private LocalDate releaseDate;

    @NotNull
    private Integer duration;

}
