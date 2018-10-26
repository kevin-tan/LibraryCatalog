DROP TABLE IF EXISTS Book;
CREATE TABLE Book (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 title varchar(255),
 publisher varchar(255),
 pubDate varchar(255),
 language varchar(255),
 isbn10 varchar(255),
 isbn13 varchar(255),
 author varchar(255),
 format varchar(255),
 pages varchar(255)
 );
