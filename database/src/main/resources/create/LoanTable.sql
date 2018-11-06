DROP TABLE IF EXISTS Loan;
CREATE TABLE Loan (
 id INTEGER PRIMARY KEY AUTOINCREMENT
 FOREIGN KEY (id) REFERENCES Item(id),
 loanTime varchar(255),
 checkoutDate varchar(255),
 dueDate varchar(255)
 );