package ru.yandex.practicum.filmorate.storage.db.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class GenreDbStorage {

    private final RowMapper<Genre> mapper;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(@Qualifier("GenreMapper") RowMapper<Genre> mapper, JdbcTemplate jdbcTemplate) {
        this.mapper = mapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Genre> getGenreById(Long id) {
        String sql = "SELECT * FROM GENRE WHERE ID = ?";
        Genre genre;
        try {
           genre = jdbcTemplate.queryForObject(sql, mapper, id);
           if (genre != null) {
               log.info("найден жанр {}", id);
               return Optional.of(genre);
           } else {
               log.info("не найден жанр {}", id);
               throw new NotFoundException("не найден жанр");
           }
        } catch (EmptyResultDataAccessException e) {
            if (log.isDebugEnabled())
                log.debug(e.getMessage());
            throw new NotFoundException("не найден жанр");
        }
    }

    public List<Genre> getGenres() {
        String sql = "SELECT * FROM GENRE LIMIT ?";
        log.info("выведен список жанров");
        return jdbcTemplate.query(sql, mapper, 100);
    }
}
