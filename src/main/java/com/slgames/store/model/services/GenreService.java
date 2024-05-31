package com.slgames.store.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slgames.store.model.Genre;
import com.slgames.store.model.GenreName;
import com.slgames.store.model.repository.GenreRepository;

import lombok.Getter;

@Service
@Getter
public class GenreService {

	
	@Autowired
	private  GenreRepository repository;
	
	
	public  Genre findByGenreName(GenreName name) {
		return getRepository().findByGenreName(name);
	}
	
	public Long getIdByGenreName(GenreName name) {
		return findByGenreName(name).getId();
	}
}
