package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Film {

    private Integer id;

    @NotBlank(message = "Название фильма пустое")
    private String name;

    @Size(max = 200, message = "Описание не больше 200 символов")
    private String description;
    private LocalDate releaseDate;

    @NotNull
    @Positive(message = "Продолжительность фильма неположительна")
    private Integer duration;

}
