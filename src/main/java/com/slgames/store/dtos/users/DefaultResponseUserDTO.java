package com.slgames.store.dtos.users;

import com.slgames.store.dtos.DefaultDTO;
import com.slgames.store.model.User;

public record DefaultResponseUserDTO(String nickname, String email) implements DefaultDTO {
	
	public DefaultResponseUserDTO(User user) {
		this(user.getNickname(), user.getEmail());
	}

}
