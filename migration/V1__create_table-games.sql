Create table enterprises (
	id Integer primary key auto_increment, 
	name_enterprise Varchar(30) Unique,
    foundation_date Date
);


Insert INTO enterprises (name_enterprise, foundation_date) Values
("PlatinumGames", "2005-06-07"), ("Square Enix", "1992-11-23")
;



create table games(
	game_id integer primary key auto_increment,
    title Varchar(50),
    price Decimal(10, 2),
    launch_date Date, 
    developer integer, 
    publisher integer,
    Foreign key (developer) references enterprises(id),
	Foreign key (publisher) references enterprises(id)
);

Insert Into games (title, launch_date, developer, publisher, price)  Values ('Nier: Automata', '2016-12-22'
, 1, 2, 77.9); 