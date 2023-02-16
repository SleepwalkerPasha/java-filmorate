package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    private Integer id;

    @Email(message = "Email is not valid")
    @NotNull
    @NotBlank(message = "email cannot be blank")
    private String email;

    @NotBlank
    @NotNull
    @Pattern(regexp = "\\S+")
    private String login;

    private String name;

    @Past
    private LocalDate birthday;
}
