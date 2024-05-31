

Create table genres(
	genre_id integer primary key auto_increment, 
    genre_name Varchar(20)
);


Insert into genres (genre_name) values ('RPG'), ('FPS'), ('ADVENTURE'), ('TERROR'), ('PUZZLE'), ('RACE')
, ('SCI_FI'), ('ACTION'); 

Create table game_genres(
    game_id integer,
    genre_id integer, 
    foreign key (game_id) references games(game_id),
    foreign key (genre_id) references genres(genre_id)
);

insert into game_genres (game_id, genre_id) values (1,1), (1,8);
    