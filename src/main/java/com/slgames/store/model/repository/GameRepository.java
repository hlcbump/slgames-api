package com.slgames.store.model.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.slgames.store.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
	
	
	public boolean existsByTitle(String title);
}
