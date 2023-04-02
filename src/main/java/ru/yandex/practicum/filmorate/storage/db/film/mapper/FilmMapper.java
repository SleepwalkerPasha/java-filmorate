package ru.yandex.practicum.filmorate.storage.db.film.mapper;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.db.film.dto.FilmDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;

@Component
@Qualifier("FilmMapper")
public class FilmMapper implements RowMapper<FilmDto> {


    @Override
    public FilmDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FilmDto(rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                Objects.requireNonNull(rs.getTimestamp("RELEASEDATE")).toLocalDateTime().toLocalDate(),
                new HashSet<>(),
                rs.getLong("MPA_ID"),
                rs.getString("MPA_NAME"),
                rs.getInt("RATE"),
                rs.getInt("DURATION"));
    }
}
