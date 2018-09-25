################
### Queries ####
################

ViewUserRegistry(): 
SELECT *
FROM User 
WHERE userType =’Admin’ 

registerUser(admin,user) : 
INSERT INTO User (firstName, lastName, physicalAddress, email, phoneNumber, userType) VALUES (‘Big’, ‘Boss’, ‘7582 Rue Concordia’, ‘bigboss@hotmail.com’, ‘514-895-9852’, ‘Admin’);

DeleteUserTable():
DROP TABLE User;

CreateUserTable():
CREATE TABLE User;

findByid(), findAll():
SELECT *
FROM User
WHERE id = 1;

update():
UPDATE User
SET firstName = 'Boss', lastName = 'Big', PhysicalAddress = '6589 Rue Concordia', email = 'bossbig@hotmail.com', phoneNumber = '514-265-9895', userType = 'Admin'
WHERE id = 1;


