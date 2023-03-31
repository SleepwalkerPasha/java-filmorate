drop table if exists FRIENDSHIP;
drop table if exists FILMGENRES;
drop table if exists MOVIELIKES;
drop table if exists USERS;
drop table if exists FILM;
drop table if exists RATINGMPA;
drop table if exists GENRE;


create table if not exists RATINGMPA
(
    ID   BIGINT auto_increment
        unique,
    NAME CHARACTER VARYING not null,
    constraint "RATINGMPA_pk"
        primary key (ID)
);

create table if not exists FILM
(
    ID          BIGINT auto_increment
        primary key
        unique,
    NAME        CHARACTER VARYING(255) not null,
    DESCRIPTION CHARACTER VARYING(255),
    RELEASEDATE TIMESTAMP              not null,
    DURATION    INTEGER                not null,
    RATE INTEGER,
    MPA_ID BIGINT not null,
        constraint "FILM_RATINGMPA_ID_FK"
        foreign key (MPA_ID) references RATINGMPA
);

create table if not exists GENRE
(
    ID   BIGINT auto_increment
        primary key
        unique,
    NAME CHARACTER VARYING(255) not null
);

create table if not exists FILMGENRES
(
    ID       BIGINT auto_increment
        primary key
        unique,
    FILM_ID  BIGINT not null,
    GENRE_ID BIGINT not null,
    constraint "FILMGENRES_FILM_ID_fk"
        foreign key (FILM_ID) references FILM,
    constraint "FILMGENRES_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRE
);

create table if not exists MOVIELIKES
(
    ID      BIGINT auto_increment
        primary key
        unique,
    USER_ID BIGINT not null,
    FILM_ID BIGINT not null
);

create table if not exists USERS
(
    ID       BIGINT auto_increment
        primary key
        unique,
    EMAIL    CHARACTER VARYING(255) not null unique,
    LOGIN    CHARACTER VARYING(255) not null unique,
    NAME     CHARACTER VARYING(255),
    BIRTHDAY TIMESTAMP
);

create table if not exists FRIENDSHIP
(
    ID       BIGINT auto_increment
        primary key
        unique,
    USERID   BIGINT                  not null
        references USERS,
    FRIENDID BIGINT                  not null
        references USERS,
    STATUS   CHARACTER VARYING not null
);

INSERT INTO RATINGMPA (NAME) VALUES ( 'G' );

INSERT INTO RATINGMPA (NAME) VALUES ( 'PG' );

INSERT INTO RATINGMPA (NAME) VALUES ( 'PG-13' );

INSERT INTO RATINGMPA (NAME) VALUES ( 'R' );

INSERT INTO RATINGMPA (NAME) VALUES ( 'NC-17' );

INSERT INTO GENRE (NAME) VALUES ( 'Комедия' );

INSERT INTO GENRE (NAME) VALUES ( 'Драма' );

INSERT INTO GENRE (NAME) VALUES ( 'Мультфильм' );

INSERT INTO GENRE (NAME) VALUES ( 'Триллер' );

INSERT INTO GENRE (NAME) VALUES ( 'Документальный' );

INSERT INTO GENRE (NAME) VALUES ( 'Боевик' );
