
INSERT INTO User (firstName, lastName, physicalAddress, email, phoneNumber, userType, password) VALUES ('Alex', 'Baker', '7582 Rue Concordia', 'AlexBaker@hotmail.com', '514-895-9852', 'Admin', '$2a$10$rnd357ZZp4s7r39sR1VlXuB5zeh/0CD8nP8qNZSEgD9P30PZphNN.');
INSERT INTO User (firstName, lastName, physicalAddress, email, phoneNumber, userType, password) VALUES ('Client0', 'Client', '7582 Rue Concordia', 'Client0@hotmail.com', '514-895-9851', 'Client', '$2a$10$rnd357ZZp4s7r39sR1VlXuB5zeh/0CD8nP8qNZSEgD9P30PZphNN.');
INSERT INTO User (firstName, lastName, physicalAddress, email, phoneNumber, userType, password) VALUES ('Client1', 'Client', '7582 Rue Concordia', 'Client1@hotmail.com', '514-895-9853', 'Client', '$2a$10$rnd357ZZp4s7r39sR1VlXuB5zeh/0CD8nP8qNZSEgD9P30PZphNN.');
INSERT INTO User (firstName, lastName, physicalAddress, email, phoneNumber, userType, password) VALUES ('Client2', 'Client', '7582 Rue Concordia', 'Client2@hotmail.com', '514-895-9854', 'Client', '$2a$10$rnd357ZZp4s7r39sR1VlXuB5zeh/0CD8nP8qNZSEgD9P30PZphNN.');
INSERT INTO User (firstName, lastName, physicalAddress, email, phoneNumber, userType, password) VALUES ('Client3', 'Client', '7582 Rue Concordia', 'Client3@hotmail.com', '514-895-9855', 'Client', '$2a$10$rnd357ZZp4s7r39sR1VlXuB5zeh/0CD8nP8qNZSEgD9P30PZphNN.');
INSERT INTO User (firstName, lastName, physicalAddress, email, phoneNumber, userType, password) VALUES ('Client4', 'Client', '7582 Rue Concordia', 'Client4@hotmail.com', '514-895-9856', 'Client', '$2a$10$rnd357ZZp4s7r39sR1VlXuB5zeh/0CD8nP8qNZSEgD9P30PZphNN.');

INSERT INTO Music (title, releaseDate, type, artist, label, asin) VALUES
('Numb', '2003-09-08', 'CD', 'Linkin Park', 'Warner Bros', 'B008F6D458'),
('Breathin', '2018-09-18', 'CD', 'Ariana Grande', 'Republic', 'C564N9H123'),
('Faded', '2015-12-03', 'CD', 'Alan Walker', 'Mer Musikk', 'T158D4DF4'),
('Nobody Can Hear You', '2017-05-19', 'CD', 'Alius ft. Ariela Jacobs', 'Record Union', 'J154D45F5'),
('Without Me', '2018-10-04', 'CD', 'Halsey', 'Capitol', 'A125BHR56');

INSERT INTO Magazine (title, publisher, pubDate, "language", isbn10, isbn13) VALUES
('ELLE', 'Kevin O''Malley', '2014-01-24', 'English', '1603200185', '978-1603200189'),
('Vogue', 'Conde Nast', '2002-04-08', 'English', '12964782516', '754-1596874532'),
('National Geographic', 'National Geographic Society', '2002-04-08', 'English', '9684352167', '858-2541698324'),
('Fashion', 'St. Joseph Media', '2010-11-14', 'English', '9654821475', '988-6581245321'),
('Time', 'Henry Luce', '1992-02-21', 'English', '8524695147', '879-3215876548');

INSERT INTO Book (title, publisher, pubDate, "language", isbn10, isbn13, author, format, pages) VALUES
('Harry Potter', 'Scholastic', '1997-06-26', 'English', '3248514751', '998-9632599842', 'J.K. Rowling', 'Paperback', '870'),
('Gone With The Wind', 'Macmillan Publishers', '1936-06-30', 'English', '9989648523', '966-8765493215', 'Margaret Mitchell', 'Paperback', '1037'),
('Pride and Prejudice', 'T. Egerton, Whitehall', '1813-01-28', 'English', '1254698325', '966-8754369512', 'Jane Austen', 'Paperback', '755'),
('To Kill a Mockingbird', 'J. B. Lippincott & Co.', '1960-07-19', 'English', '0061120081', '978-0061120084', 'Harper Lee', 'Paperback', '281'),
('The Picture of Dorian Gray', 'Random House', '2004-06-01', 'English', '0375751513', '978-0375751516', 'Oscar Wilde', 'Paperback', '351');

INSERT INTO Movie (title, releaseDate, director, "language", subtitles, runtime) VALUES
('Guardians of the Galaxy', '2014-07-21', 'James Gunn', 'English', 'English', '122 minutes'),
('Inception', '2010-07-08', 'Christopher Nolan', 'English', 'English', '148 minutes'),
('Titanic', '1997-11-01', 'James Cameron', 'English', 'Japanese', '195 minutes'),
('Star Wars', '1977-05-25', 'George Lucas', 'English', 'French', '121 minutes'),
('The Lord of the Rings: The Fellowship of the Ring', '2001-12-19', 'Peter Jackson', 'English', 'English', '178 minutes');

INSERT INTO Producers (movieId, producer) VALUES
('1', 'Kevin Feige'),
('2', 'Emma Thomas'),
('3', 'Jon Landau'),
('4', 'Gary Kurtz'),
('5', 'Barrie M. Osborne'),
('5', 'Peter Jackson'),
('5', 'Fran Walsh'),
('5', 'Tim Sanders');

INSERT INTO Dubbed (movieId, dub) VALUES
('1', 'French'),
('2', 'German'),
('3', 'French'),
('4', 'Chinese'),
('5', 'French');

INSERT INTO Actors (movieId, actor) VALUES
('1', 'Chris Pratt'),
('1', 'Zoe Saldana'),
('1', 'Dave Bautista'),
('1',  'Vin Diesel'),
('1', 'Bradley Cooper'),
('2', 'Leonardo DiCaprio'),
('2', 'Ken Watanabe'),
('2', 'Joseph Gordon-Levitt'),
('2', 'Marion Cotillard'),
('3', 'Leonardo DiCaprio'),
('3', 'Kate Winslet'),
('3', 'Billy Zane'),
('3', 'Kathy Bates'),
('4', 'Mark Hamill'),
('4', 'Harrison Ford'),
('4', 'Carrie Fisher'),
('4', 'Peter Cushing'),
('5', 'Ian McKellen'),
('5', 'Viggo Mortensen'),
('5', 'Liv Tyler'),
('5', 'Elijah Wood');

INSERT INTO Item (itemSpecId, type) VALUES
(1, 'Movie'),
(2, 'Movie'),
(3, 'Movie'),
(4, 'Movie'),
(5, 'Movie'),
(1, 'Magazine'),
(2, 'Magazine'),
(3, 'Magazine'),
(4, 'Magazine'),
(5, 'Magazine'),
(1, 'Music'),
(2, 'Music'),
(3, 'Music'),
(4, 'Music'),
(5, 'Music'),
(1, 'Book'),
(2, 'Book'),
(3, 'Book'),
(4, 'Book'),
(5, 'Book');

INSERT INTO LoanableItem (id, available)
SELECT id, TRUE FROM Item
WHERE Item.type != 'Magazine';

UPDATE LoanableItem SET userId = 2 , available = FALSE WHERE id = 1;
UPDATE LoanableItem SET userId = 2 , available = FALSE WHERE id = 11;
UPDATE LoanableItem SET userId = 2 , available = FALSE WHERE id = 16;

INSERT INTO LoanTransaction (itemId, userId, transactionDate, dueDate) VALUES
(1,2, DateTime('now'),DateTime('now')),
(11,2, DateTime('now'),DateTime('now')),
(16,2, DateTime('now'),DateTime('now'));

INSERT INTO ReturnTransaction (itemId, userId, transactionDate) VALUES
(1,2, DateTime('now')),
(11,2, DateTime('now')),
(16,2, DateTime('now'));