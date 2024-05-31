package com.slgames.store.dtos.game;

import java.time.LocalDate;
import java.util.Set;

import com.slgames.store.dtos.genre.GenreDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record InsertGameDTO(
		@NotBlank String title,
		@NotNull @PastOrPresent LocalDate launchDate, 
		@NotNull Double price, 
		@NotNull Long developer, 
		@NotNull Long publisher,
		@NotNull Set<GenreDTO> genres
		) {

	
	@Override
	public final String toString() {
		return "{"
				+ "\"title\":" + "\"" + title + "\""
				+ "\n,\"launchDate\":" + (launchDate != null? "\"" + launchDate + "\"":launchDate)
				+ "\n,\"price\":" + price
				+ "\n,\"publisher\":" + publisher
				+ "\n,\"developer\":" + developer
				+ "\n,\"genres\":" + genres.toString()
				+ "}";
				
	}
}
