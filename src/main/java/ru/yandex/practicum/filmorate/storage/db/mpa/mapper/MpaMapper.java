package ru.yandex.practicum.filmorate.storage.db.mpa.mapper;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Qualifier("MpaMapper")
public class MpaMapper implements RowMapper<MpaRating> {
    @Override
    public MpaRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MpaRating(rs.getLong("ID"), rs.getString("NAME"));
    }
}
