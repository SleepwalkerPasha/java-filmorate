package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.Data;
import java.time.LocalDate;

@Data
public class User {

    private Integer id;

    @Email(message = "Email is not valid")
    @NotBlank(message = "email cannot be blank")
    private String email;

    @NotBlank
    @Pattern(regexp = "\\S+")
    private String login;

    private String name;

    @Past
    private LocalDate birthday;
}
