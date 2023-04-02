package ru.yandex.practicum.filmorate.storage.db.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.db.film.dto.FilmGenresDto;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final RowMapper<FilmDto> filmRowMapper;

    private final RowMapper<Genre> genreRowMapper;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(@Qualifier("FilmMapper") RowMapper<FilmDto> filmRowMapper,
                         @Qualifier("GenreMapper") RowMapper<Genre> genreRowMapper,
                         JdbcTemplate jdbcTemplate) {
        this.filmRowMapper = filmRowMapper;
        this.genreRowMapper = genreRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        String sql = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA_ID) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        int countOfRows = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(film.getReleaseDate(), LocalTime.NOON)));
            ps.setInt(4, film.getDuration());
            if (film.getRate() == null)
                ps.setInt(5, 0);
            else
                ps.setInt(5, film.getRate());
            ps.setLong(6, film.getMpa().getId());
            return ps;
        }, key);
        long keyValue = Objects.requireNonNull(key.getKey()).longValue();
        film.setId(keyValue);
        boolean genres = true;
        if (!film.getGenres().isEmpty())
            genres = insertIntoFilmGenres(film, keyValue);
        if (!genres && countOfRows == 0) {
            log.info("произошла ошибка при добавлении значений film, такой уже существует");
            return Optional.empty();
        }
        log.info("добавлена запись о фильме name: {}", film.getName());
        return Optional.of(film);
    }


    private boolean insertIntoFilmGenres(Film film, long filmId) {
        if (film.getGenres().isEmpty())
            return true;
        String insertGenre = "INSERT INTO FILMGENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        List<Object[]> batch = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            Object[] values = new Object[]{
                    filmId,
                    genre.getId()
            };
            batch.add(values);
        }
        int[] counts = jdbcTemplate.batchUpdate(insertGenre, batch);
        if (counts.length == 1) {
            log.info("не записаны genres");
            return false;
        }
        return true;
    }

    @Override
    public Optional<Film> updateFilm(Film newFilm) {
        Optional<Film> film = getFilmById(newFilm.getId());
        if (film.isPresent()) {
            deleteFilmFromFilmGenres(newFilm.getId());
            insertIntoFilmGenres(newFilm, newFilm.getId());
        }
        String sql = "UPDATE FILM SET NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATE = ?, MPA_ID = ?" +
                "WHERE ID = ?";
        int count = jdbcTemplate.update(sql, newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(),
                newFilm.getDuration(), newFilm.getRate(), newFilm.getMpa().getId(), newFilm.getId());
        if (count == 0) {
            log.info("произошла ошибка при обновлении значений");
            return Optional.empty();
        }
        log.info("обновлен фильм id {}", newFilm.getId());
        return getFilmById(newFilm.getId());
    }

    @Override
    public void deleteFilm(long filmId) {
        if (getFilmById(filmId).isPresent()) {
            deleteFilmFromMovieLikes(filmId);
            deleteFilmFromFilmGenres(filmId);
            deleteFilmFromFilm(filmId);
        }
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        String sql = "SELECT * FROM FILM AS F INNER JOIN RATINGMPA AS R ON R.ID = F.MPA_ID WHERE F.ID = ?";
        FilmDto dto;
        Film film;
        try {
            dto = jdbcTemplate.queryForObject(sql, filmRowMapper, id);
            if (dto != null) {
                film = new Film(dto.getId(), dto.getName(), dto.getDescription(), dto.getReleaseDate(),
                        new MpaRating(dto.getMpa(), dto.getMpaName()), dto.getRate(), dto.getDuration());
                if (dto.getGenres().isEmpty())
                    film.setGenres(getGenresOfFilm(id).stream().sorted(Comparator.comparing(Genre::getId))
                            .collect(Collectors.toCollection(LinkedHashSet::new)));
                log.info("найден фильм {} {}", film.getId(), film.getName());
                return Optional.of(film);
            } else {
                log.info("фильм не найден {}", id);
                return Optional.empty();
            }
        } catch (EmptyResultDataAccessException e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
            return Optional.empty();
        }
    }

    private List<Genre> getGenresOfFilm(long id) {
        String sql = "SELECT * FROM GENRE WHERE ID IN " +
                "(SELECT DISTINCT GENRE_ID FROM FILMGENRES AS FG WHERE FG.FILM_ID = ?)";
        return jdbcTemplate.query(sql, genreRowMapper, id);
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT F.ID AS FILM_ID, F.NAME AS FILM_NAME, DESCRIPTION, RELEASEDATE, MPA_ID," +
                " R.MPA_NAME AS MPA_NAME, RATE, DURATION FROM FILM AS F INNER JOIN RATINGMPA AS R ON F.MPA_ID = R.ID";
        log.info("выведен список всех фильмов");
        List<FilmDto> filmsDtos = jdbcTemplate.query(sql, filmRowMapper);
        List<Film> films = new ArrayList<>();
        List<FilmGenresDto> filmGenresDtos = jdbcTemplate.query("SELECT FILM_ID, GENRE_ID, G.NAME AS GENRE_NAME " +
                        "FROM FILMGENRES " +
                        "AS FG INNER JOIN GENRE AS G " +
                        "ON FG.GENRE_ID = G.ID",
                ((rs, rowNum) ->
                        new FilmGenresDto(rs.getLong("FILM_ID"),
                                rs.getLong("GENRE_ID"),
                                rs.getString("GENRE_NAME"))));
        for (FilmDto dto : filmsDtos) {
            MpaRating rating = new MpaRating(dto.getMpa(), dto.getMpaName());
            Film film = new Film(dto.getId(), dto.getName(), dto.getDescription(), dto.getReleaseDate(),
                    rating, dto.getRate(), dto.getDuration());

            Set<Genre> genres = new LinkedHashSet<>();
            for (FilmGenresDto filmGenresDto : filmGenresDtos) {
                if (filmGenresDto.getFilmId().equals(dto.getId())) {
                    genres.add(new Genre(filmGenresDto.getGenreId(), filmGenresDto.getGenreName()));
                }
            }
            film.setGenres(genres);
            films.add(film);
        }
        return films;
    }

    @Override
    public boolean addUserLike(Long userId, Long filmId) {
        String sql = "INSERT INTO MOVIELIKES (USER_ID, FILM_ID) VALUES (?, ?)";
        int countOfRows = jdbcTemplate.update(sql, userId, filmId);
        if (countOfRows == 0) {
            log.info("произошла ошибка при добавлении лайка");
            return false;
        }
        log.info("пользователь {} поставил лайк {}", userId, filmId);
        return true;
    }

    @Override
    public boolean removeUserLike(Long userId, Long filmId) {
        String sql = "DELETE FROM MOVIELIKES WHERE USER_ID = ? AND FILM_ID = ?";
        int countOfRows = jdbcTemplate.update(sql, userId, filmId);
        if (countOfRows == 0) {
            log.info("произошла ошибка при удалении лайка");
            return false;
        }
        log.info("пользователь {} удалил лайк {}", userId, filmId);
        return true;
    }

    @Override
    public List<Film> getTopTenPopularFilmsByLikes(Long count) {
        String sql = "SELECT F.ID AS FILM_ID, F.NAME AS FILM_NAME, DESCRIPTION, RELEASEDATE, MPA_ID," +
                "R.MPA_NAME AS MPA_NAME, RATE, DURATION FROM FILM AS F INNER JOIN RATINGMPA AS R ON R.ID = F.MPA_ID " +
                "WHERE F.ID IN (SELECT F.ID FROM MOVIELIKES LEFT JOIN FILM AS F ON F.ID = MOVIELIKES.FILM_ID" +
                " GROUP BY F.ID ORDER BY COUNT(USER_ID) DESC LIMIT ?)";
        log.info("вывод топ фильмов по лайкам");
        List<FilmDto> list = jdbcTemplate.query(sql, filmRowMapper, count);
        if (list.isEmpty())
            return getFilms().stream().limit(count).collect(Collectors.toList());
        List<Film> films = new ArrayList<>();
        for (FilmDto dto : list) {
            films.add(new Film(dto.getId(), dto.getName(), dto.getDescription(), dto.getReleaseDate(),
                    new MpaRating(dto.getMpa(), dto.getMpaName()), dto.getRate(), dto.getDuration()));
        }
        return films;
    }


    private void deleteFilmFromMovieLikes(long filmId) {
        String deleteFromFilmGenres = "DELETE FROM MOVIELIKES WHERE FILM_ID = ?";
        int count = jdbcTemplate.update(deleteFromFilmGenres, filmId);
        if (count == 0)
            log.info("нет фильма с данным id из MOVIELIKES");
        else
            log.info("удален фильм с id {} из MOVIELIKES", filmId);
    }

    private void deleteFilmFromFilmGenres(long filmId) {
        String deleteFromFilmGenres = "DELETE FROM FILMGENRES WHERE FILM_ID = ?";
        int count = jdbcTemplate.update(deleteFromFilmGenres, filmId);
        if (count == 0)
            log.info("нет фильма с данным id из FILMGENRES");
        else
            log.info("удален фильм с id {} из FILMGENRES", filmId);
    }

    private void deleteFilmFromFilm(long filmId) {
        String deleteFromFilmGenres = "DELETE FROM FILM WHERE ID = ?";
        int count = jdbcTemplate.update(deleteFromFilmGenres, filmId);
        if (count == 0)
            log.info("нет фильма с данным id из FILM");
        else
            log.info("удален фильм с id {} из FILM", filmId);
    }

}
