package com.slgames.store.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdateUserDTO(
		@NotNull
		Long id,
		String nickname,
		@Email
		String email,
		String password) {

}
