CREATE TABLE roles(
	id INTEGER primary key auto_increment,
    `name` Varchar(10) NOT NULL
);

INSERT INTO roles (`name`) VALUES ('DEFAULT'),('STAFF'),('ADM');


CREATE TABLE users (
	id INTEGER primary key auto_increment, 
    nickname Varchar(40) , 
    email Varchar(50) NOT NULL, 
    `password` Varchar(20) NOT NULL, 
    `role` Integer NOT NULL, 
    FOREIGN KEY (`role`) references roles(id)
); 