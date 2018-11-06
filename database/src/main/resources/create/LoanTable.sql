DROP TABLE IF EXISTS Loan;
CREATE TABLE Loan (
 FOREIGN KEY (id) REFERENCES Item(id),
 loanType varchar(255),
 loanDate varchar(255),
 );