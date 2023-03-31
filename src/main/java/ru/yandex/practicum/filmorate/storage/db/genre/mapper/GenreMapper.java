package ru.yandex.practicum.filmorate.storage.db.genre.mapper;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Qualifier("GenreMapper")
public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getLong("ID"), rs.getString("NAME"));
    }
}
