package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.genre.GenreDbStorage;

import java.util.List;
import java.util.Optional;

@RestController
public class GenreController {

    private final GenreDbStorage storage;

    @Autowired
    public GenreController(GenreDbStorage storage) {
        this.storage = storage;
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        Optional<Genre> genreOptional = storage.getGenreById(id);
        if (genreOptional.isEmpty())
            throw new NotFoundException("не найден жанр с id = " + id);
        return genreOptional.get();
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return storage.getGenres();
    }
}
