DROP TABLE IF EXISTS Item;
CREATE TABLE Item (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 itemSpecId INTEGER ,
 type varchar(255),
 available varchar(255)
 );