package com.slgames.store.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slgames.store.model.Genre;
import com.slgames.store.model.GenreName;

public interface GenreRepository extends JpaRepository<Genre, Long>{

	public Genre findByGenreName (GenreName name);
	
}
