package ru.yandex.practicum.filmorate.storage.db.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MpaRating> mapper;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("MpaMapper") RowMapper<MpaRating> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }


    public Optional<MpaRating> getRatingMPAById(Long id) {
        String sql = "SELECT * FROM RATINGMPA WHERE ID = ?";
        MpaRating rating;
        try {
            rating = jdbcTemplate.queryForObject(sql, mapper, id);
            if (rating != null) {
                log.info("найден рейтинг {}", id);
                return Optional.of(rating);
            } else {
                log.info("не найден рейтинг {}", id);
                throw new NotFoundException("Рейтинг не найден");
            }
        } catch (EmptyResultDataAccessException e) {
            if (log.isDebugEnabled())
                log.debug(e.getMessage());
            throw new NotFoundException("Рейтинг не найден");
        }
    }

    public List<MpaRating> getRatingMPAList() {
        String sql = "SELECT * FROM RATINGMPA LIMIT ?";
        log.info("выведен список рейтингов");
        return jdbcTemplate.query(sql, mapper, 100);
    }
}
