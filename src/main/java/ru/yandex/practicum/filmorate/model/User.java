package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    private Integer id;

    @Email(message = "Электронная почта невалидна")
    @NotNull
    @NotBlank(message = "Электронная почта пустая")
    private String email;

    @NotBlank
    @NotNull
    @Pattern(regexp = "\\S+")
    private String login;

    private String name;

    @Past(message = "День рождения должен быть в прошлом")
    private LocalDate birthday;
}
