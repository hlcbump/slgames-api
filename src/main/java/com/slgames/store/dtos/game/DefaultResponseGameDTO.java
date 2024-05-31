package com.slgames.store.dtos.game;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;


import com.slgames.store.dtos.DefaultDTO;
import com.slgames.store.dtos.genre.GenreDTO;
import com.slgames.store.model.Game;

public record DefaultResponseGameDTO(Long id, 
		String title, 
		LocalDate launchDate, 
		Double price, 
		Set<GenreDTO> genres) implements DefaultDTO{

	
	public DefaultResponseGameDTO(Game game) {
		this(game.getId(),game.getTitle(), game.getLaunchDate(), game.getPrice(), game.getGenres().stream().map(genre -> new GenreDTO(genre)).collect(Collectors.toSet()));
	}
}
