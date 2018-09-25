DROP TABLE IF EXISTS User;
CREATE TABLE User (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 firstName varchar(255),
 lastName varchar(255),
 physicalAddress char(255),
 email varchar(255) NOT NULL UNIQUE,
 phoneNumber varchar(255) NOT NULL UNIQUE,
 userType varchar(6)
);
