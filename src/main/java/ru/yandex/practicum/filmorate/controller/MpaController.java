package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.db.mpa.MpaDbStorage;

import java.util.List;
import java.util.Optional;

@RestController
public class MpaController {

    private final MpaDbStorage storage;

    @Autowired
    public MpaController(MpaDbStorage storage) {
        this.storage = storage;
    }

    @GetMapping("/mpa/{id}")
    public MpaRating getRatingMPAById(@PathVariable Long id) {
        Optional<MpaRating> ratingOptional = storage.getRatingMPAById(id);
        return ratingOptional.orElse(new MpaRating(999L, "undefined"));
    }

    @GetMapping("/mpa")
    public List<MpaRating> getRatingMPAList() {
        return storage.getRatingMPAList();
    }
}
