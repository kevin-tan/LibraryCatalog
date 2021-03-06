DROP TABLE IF EXISTS ReturnTransaction;
CREATE TABLE ReturnTransaction (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 itemId INTEGER NOT NULL,
 userId INTEGER NOT NULL,
 transactionDate TIMESTAMP,
 FOREIGN KEY (itemId) REFERENCES LoanableItem(id),
 FOREIGN KEY (userId) REFERENCES User(id)
 );