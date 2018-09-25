###################
## SQLite Script ##
###################


DROP TABLE IF EXISTS User;

#User Table
CREATE TABLE User (
 id int NOT NULL AUTO_INCREMENT,
 firstName varchar(255),
 lastName varchar(255),
 physicalAddress char(255),
 email varchar(255) NOT NULL UNIQUE,
 phoneNumber varchar(255) NOT NULL UNIQUE,
 userType varchar(6),
 PRIMARY KEY (id)
);
