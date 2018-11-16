DROP TABLE IF EXISTS LoanableItem;
CREATE TABLE LoanableItem (
 id INTEGER PRIMARY KEY,
 userId INTEGER,
 available BOOLEAN NOT NULL,
 FOREIGN KEY (id) REFERENCES Item(id),
 FOREIGN KEY (userId) REFERENCES User(id)
 );
