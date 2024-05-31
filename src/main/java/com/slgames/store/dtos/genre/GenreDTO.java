package com.slgames.store.dtos.genre;

import com.slgames.store.model.Genre;
import com.slgames.store.model.GenreName;

public record GenreDTO(GenreName genreName) {

	public GenreDTO(Genre genre) {
		this(genre.getGenreName());
	}
	@Override
	public final String toString() {
		return "{"
				+ "\"genreName\":" + "\"" + genreName + "\""
				+ "}";
	}
}
