package com.slgames.store.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InsertUserDTO
(String nickname,
		@NotBlank
		@Email
		String email, 
		@NotBlank
		String password,
		
		@NotBlank
		String role) {

	
	@Override
	public String toString() {
		return "{"
				+ "\"nickname\":\"" + nickname 
				+ "\",\n\"email\":\"" + email
				+ "\",\n\"password\":\"" + password
				+ "\",\n\"role\":\"" + role
				+ "\"\n}";
	}
}
