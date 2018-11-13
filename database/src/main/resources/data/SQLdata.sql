
INSERT INTO User (firstName, lastName, physicalAddress, email, phoneNumber, userType, password) VALUES ('Alex', 'Baker', '7582 Rue Concordia', 'AlexBaker@hotmail.com', '514-895-9852', 'Admin', '$2a$10$rnd357ZZp4s7r39sR1VlXuB5zeh/0CD8nP8qNZSEgD9P30PZphNN.');
INSERT INTO User (firstName, lastName, physicalAddress, email, phoneNumber, userType, password) VALUES ('Client', 'Client', '7582 Rue Concordia', 'Client@hotmail.com', '514-895-9851', 'Client', '$2a$10$rnd357ZZp4s7r39sR1VlXuB5zeh/0CD8nP8qNZSEgD9P30PZphNN.');

INSERT INTO Music (title, releaseDate, type, artist, label, asin) VALUES
('Numb', 'September 8, 2003', 'CD', 'Linkin Park', 'Warner Bros', 'B008F6D458'),
('Breathin', 'September 18, 2018', 'CD', 'Ariana Grande', 'Republic', 'C564N9H123'),
('Faded', 'December 3, 2015', 'CD', 'Alan Walker', 'Mer Musikk', 'T158D4DF4'),
('Nobody Can Hear You', 'May 19, 2017', 'CD', 'Alius ft. Ariela Jacobs', 'Record Union', 'J154D45F5'),
('Without Me', 'October 4, 2018', 'CD', 'Halsey', 'Capitol', 'A125BHR56');

INSERT INTO Magazine (title, publisher, pubDate, "language", isbn10, isbn13) VALUES
('ELLE', 'Kevin O''Malley', 'January 24, 2014', 'English', '1603200185', '978-1603200189'),
('Vogue', 'Conde Nast', 'April 8, 2002', 'English', '12964782516', '754-1596874532'),
('National Geographic', 'National Geographic Society', 'April 8, 2002', 'English', '9684352167', '858-2541698324'),
('Fashion', 'St. Joseph Media', 'November 14, 2010', 'English', '9654821475', '988-6581245321'),
('Time', 'Henry Luce', 'February 21, 1992', 'English', '8524695147', '879-3215876548');

INSERT INTO Book (title, publisher, pubDate, "language", isbn10, isbn13, author, format, pages) VALUES
('Harry Potter', 'Scholastic', 'June 26, 1997', 'English', '3248514751', '998-9632599842', 'J.K. Rowling', 'Paperback', '870'),
('Gone With The Wind', 'Macmillan Publishers', 'June 30, 1936', 'English', '9989648523', '966-8765493215', 'Margaret Mitchell', 'Paperback', '1037'),
('Pride and Prejudice', '	T. Egerton, Whitehall', 'January 28, 1813', 'English', '1254698325', '966-8754369512', 'Jane Austen', 'Paperback', '755'),
('To Kill a Mockingbird', '	J. B. Lippincott & Co.', 'July 19, 1960', 'English', '0061120081', '978-0061120084', 'Harper Lee', 'Paperback', '281'),
('The Picture of Dorian Gray', 'Random House', 'June 1, 2004', 'English', '0375751513', '978-0375751516', 'Oscar Wilde', 'Paperback', '351');

INSERT INTO Movie (title, releaseDate, director, "language", subtitles, runtime) VALUES
('Guardians of the Galaxy', 'July 21, 2014', 'James Gunn', 'English', 'English', '122 minutes'),
('Inception', 'July 8, 2010', 'Christopher Nolan', 'English', 'English', '148 minutes'),
('Titanic', 'November 1, 1997', 'James Cameron', 'English', 'Japanese', '195 minutes'),
('Star Wars', 'May 25, 1977', 'George Lucas', 'English', 'French', '121 minutes'),
('The Lord of the Rings: The Fellowship of the Ring', 'December 19, 2001', 'Peter Jackson', 'English', 'English', '178 minutes');

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

-- INSERT INTO Loanable (itemId)
-- SELECT id FROM Item
-- WHERE Item.type != 'Magazine';

INSERT INTO Loanable (itemId, checkoutDate, dueDate) VALUES
(1,DateTime('now'),DateTime('now'));

INSERT INTO Transac (loanableId, userId, transactionType) VALUES
(1,2,'Loan');