package com.slgames.store.controllers;

import java.util.List;
import java.util.Optional;

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

import com.slgames.store.dtos.TypeDTO;
import com.slgames.store.dtos.users.DefaultResponseUserDTO;
import com.slgames.store.dtos.users.InsertUserDTO;
import com.slgames.store.dtos.users.UpdateUserDTO;
import com.slgames.store.dtos.users.UserDTOFactory;
import com.slgames.store.model.User;
import com.slgames.store.model.services.UserService;

import jakarta.validation.Valid;
import lombok.Getter;

@RestController
@RequestMapping("/user")
@Getter
public class UserController {

	@Autowired
	private UserService service;
	
	@GetMapping
	public ResponseEntity<List<DefaultResponseUserDTO>> findAll() {
		return ResponseEntity.ok(getService().findAll().stream()
				.map(user -> (DefaultResponseUserDTO) UserDTOFactory.createDTO(user, TypeDTO.DEFAULT)).toList());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		Optional<User> optional = getService().findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(UserDTOFactory.createDTO(optional.get(), TypeDTO.DEFAULT));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<?> insertUser(@RequestBody @Valid InsertUserDTO dto, UriComponentsBuilder builder){
		User user = getService().createUser(dto);
		var uri = builder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body(UserDTOFactory.createDTO(user, TypeDTO.CREATED));
	}
	
	@PutMapping
	public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO dto){
		User user = getService().updateUser(dto);
		if (user != null) {
			return ResponseEntity.ok(UserDTOFactory.createDTO(user, TypeDTO.DEFAULT));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id){
		if (getService().deleteUser(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
}
