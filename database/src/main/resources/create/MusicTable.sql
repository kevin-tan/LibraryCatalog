DROP TABLE IF EXISTS Music;
CREATE TABLE Music (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 title varchar(255),
 releaseDate varchar(255),
 type varchar(255),
 artist varchar(255),
 label varchar(255),
 asin varchar(255) UNIQUE
 );