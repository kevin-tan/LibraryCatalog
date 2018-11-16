DROP TABLE IF EXISTS LoanTransaction;
CREATE TABLE LoanTransaction (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 itemId INTEGER NOT NULL,
 userId INTEGER NOT NULL,
 transactionDate TIMESTAMP,
 dueDate TIMESTAMP,
 FOREIGN KEY (itemId) REFERENCES Item(id),
 FOREIGN KEY (userId) REFERENCES User(id)
 );