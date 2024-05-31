package com.slgames.store.dtos.game;

import com.slgames.store.dtos.AbstractDTOFactory;
import com.slgames.store.dtos.CreatedDTO;
import com.slgames.store.dtos.DTO;
import com.slgames.store.dtos.DefaultDTO;
import com.slgames.store.dtos.TypeDTO;
import com.slgames.store.model.Game;

import lombok.Getter;

@Getter
public class GameDTOFactory implements AbstractDTOFactory<Game> {
	
	public static GameDTOFactory INSTANCE = new GameDTOFactory();

	public synchronized static GameDTOFactory getInstance() {
			if (INSTANCE == null) INSTANCE = new GameDTOFactory();
			return INSTANCE;
	}
	public static DTO createDTO(Game game, TypeDTO type) {
		switch (type) {
			case TypeDTO.CREATED:
				return getInstance().fabricateCreated(game);
			case TypeDTO.DEFAULT:
				return getInstance().fabricateDefault(game);
			default:
				return null;
		}
	}
	
	@Override
	public CreatedDTO fabricateCreated(Game object) {
		return new CreatedResponseGameDTO(object);
	}

	@Override
	public DefaultDTO fabricateDefault(Game object) {
		return new DefaultResponseGameDTO(object);
	}

}
