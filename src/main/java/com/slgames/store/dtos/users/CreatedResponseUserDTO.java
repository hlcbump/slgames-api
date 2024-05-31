package com.slgames.store.dtos.users;


import com.slgames.store.dtos.CreatedDTO;
import com.slgames.store.model.User;

public record CreatedResponseUserDTO(String nickname, String email) implements CreatedDTO{

	
	public CreatedResponseUserDTO(User user) {
		this(user.getNickname(), user.getEmail());
	}
	
}
