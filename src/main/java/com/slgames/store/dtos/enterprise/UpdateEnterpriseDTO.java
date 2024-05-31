package com.slgames.store.dtos.enterprise;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record UpdateEnterpriseDTO(
		@NotNull Long id,
		String name, 
		LocalDate foundationDate) {
	
	@Override
	public final String toString() {
		return "{"
				+ "\"id\":" + id
				+ "\n,\"name\":" + "\"" +name+"\""
				+ "\n,\"foundationDate\":" + (foundationDate != null?"\"" + foundationDate + "\"": foundationDate)
				+ "}";
	}

}
