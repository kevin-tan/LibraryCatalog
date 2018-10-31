DROP TABLE IF EXISTS Magazine;
CREATE TABLE Magazine (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 title varchar(255),
 publisher varchar(255),
 pubDate varchar(255),
 language varchar(255),
 isbn10 varchar(255) UNIQUE,
 isbn13 varchar(255) UNIQUE
 );
