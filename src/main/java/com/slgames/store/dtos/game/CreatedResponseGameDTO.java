package com.slgames.store.dtos.game;

import com.slgames.store.dtos.CreatedDTO;
import com.slgames.store.model.Game;

public record CreatedResponseGameDTO(
		Long id, 
		String title) implements CreatedDTO {

	
	public CreatedResponseGameDTO(Game game) {
		this(game.getId(), game.getTitle());
	}
}
