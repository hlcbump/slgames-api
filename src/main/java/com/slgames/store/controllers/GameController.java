package com.slgames.store.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.slgames.store.dtos.game.DefaultResponseGameDTO;
import com.slgames.store.dtos.game.GameDTOFactory;
import com.slgames.store.dtos.game.InsertGameDTO;
import com.slgames.store.dtos.game.UpdateGameDTO;
import com.slgames.store.model.Game;
import com.slgames.store.model.services.GameService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Getter;

@RestController
@RequestMapping("/game")
@Getter
public class GameController {
	
	
	@Autowired
	private GameService service;
	
	
	@GetMapping
	public ResponseEntity<?> findAllGames(){
		return ResponseEntity.ok(getService().findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Game> findGameById(@PathVariable Long id){
		return ResponseEntity.of(getService().findById(id));
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> insertGame(@RequestBody @Valid InsertGameDTO gameDto, UriComponentsBuilder builder){
			Game game = getService().createGame(gameDto);
			var uri = builder.path("/game/{id}").buildAndExpand(game.getId()).toUri();
			return ResponseEntity.created(uri).body(GameDTOFactory.getInstance().fabricateCreated(game));
	}
	@PutMapping
	@Transactional
	public ResponseEntity<?> updateGame(@RequestBody @Valid UpdateGameDTO gameDto){
		Game game = getService().update(gameDto);
		if (game != null) {
			return ResponseEntity.ok().body(new DefaultResponseGameDTO(game));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteGame(@PathVariable Long id){
		if (getService().delete(id)) return ResponseEntity.noContent().build();
		else return ResponseEntity.notFound().build();
	}
}