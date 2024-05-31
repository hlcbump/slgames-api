


Create table if not exists enterprises (
	id Integer primary key auto_increment, 
	name_enterprise Varchar(30) Unique,
    foundation_date Date
);

create table if not exists games(
	game_id integer primary key auto_increment,
    title Varchar(50) Unique,
    price Decimal(10, 2),
    launch_date Date, 
    developer integer, 
    publisher integer,
    Foreign key (developer) references enterprises(id),
	Foreign key (publisher) references enterprises(id)
);

