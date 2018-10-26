DROP TABLE IF EXISTS Movie;
DROP TABLE IF EXISTS Producers;
DROP TABLE IF EXISTS Actors;
DROP TABLE IF EXISTS Dubbed;

CREATE TABLE Movie (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 title varchar(255),
 releaseDate varchar(255),
 director varchar(255),
 language varchar(255),
 subtitles varchar(255),
 runtime varchar(255)
 );

CREATE TABLE Producers(
    _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    movieId INTEGER,
    producer varchar(255),
    UNIQUE(movieId, producer)
FOREIGN KEY(movieId) REFERENCES Movie(id)
);

CREATE TABLE Actors(
    _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    movieId INTEGER,
    actors varchar(255),
    UNIQUE(movieId, actors)
FOREIGN KEY(movieId) REFERENCES Movie(id)
);

CREATE TABLE Dubbed(
    _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    movieId INTEGER,
    dub varchar(255),
    UNIQUE(movieId, dub)
FOREIGN KEY(movieId) REFERENCES Movie(id)
);
