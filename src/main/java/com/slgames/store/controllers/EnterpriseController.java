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

import com.slgames.store.dtos.TypeDTO;
import com.slgames.store.dtos.enterprise.EnterpriseDTOFactory;
import com.slgames.store.dtos.enterprise.InsertEnterpriseDTO;
import com.slgames.store.dtos.enterprise.UpdateEnterpriseDTO;
import com.slgames.store.model.Enterprise;
import com.slgames.store.model.services.EnterpriseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Getter;

@RestController
@RequestMapping("/enterprise")
@Getter
@Tag(name = "/enterprise", description = "This is the endpoint to manipulate and acess enterprises data.")
public class EnterpriseController {
	
	
	@Autowired
	private EnterpriseService service;
	
	@GetMapping
	@Operation(summary = "Return all enterprises stored on the API, showing a Default DTO object of them.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Show all enterprises data stored.")
	})
	public ResponseEntity<?> findAll(){
		return ResponseEntity.ok(getService().findAll());
	}
	@GetMapping("/{id}")
	@Operation(summary = "Return a specific enterprise based on your ID. If not exists, will return HTTP status 404.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Show the enterprise data."),
			@ApiResponse(responseCode = "404", description = "Not found.")
	})
	public ResponseEntity<?> findEnterpriseById(@PathVariable Long id){
		return ResponseEntity.of(getService().findById(id));
	}
	
	@PostMapping
	@Transactional
	@Operation(summary = "Insert a new enterprise in the database.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description= "The enterprise data provided is created." ),
			@ApiResponse(responseCode = "400", description= "The enterprise data is not allowed, because some enterprise with the same name exists.")
	})
	public ResponseEntity<?> insertEnterprise(@RequestBody @Valid InsertEnterpriseDTO enterpriseDto, UriComponentsBuilder builder ){
		Enterprise enterprise = getService().createEnterprise(enterpriseDto);
		var uri = builder.path("/enterprise/{id}").buildAndExpand(enterprise.getId()).toUri();
		return ResponseEntity.created(uri).body(EnterpriseDTOFactory.getDTO(enterprise, TypeDTO.CREATED));
	}
	
	@PutMapping
	@Transactional
	@Operation(summary = "Update a atribute of a enterprise. Only allows by the DTO the updating of name and/or foundationDate.")
	@ApiResponses(value = {
			@ApiResponse(responseCode="200", description="The enterprise data was updated."), 
			@ApiResponse(responseCode="400", description="Could occur when Id is not provided."),
			@ApiResponse(responseCode="404", description="The ID was not found in the database."),
			@ApiResponse(responseCode = "500", description = "Could occur when the consumer tries to update a name of a enterprise with a existing name in database.")
	})
	public ResponseEntity<?> updateEnterprise(@RequestBody @Valid UpdateEnterpriseDTO enterpriseDto){
		Enterprise enterprise = getService().update(enterpriseDto);
		if (enterprise != null) {
			return ResponseEntity.ok(EnterpriseDTOFactory.getDTO(enterprise, TypeDTO.DEFAULT));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@Operation(summary = "Delete a enterprise based on your Id. Could return 500 when Games with the referncing foreign keys is related to the enterprise.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "The enterprise was deleted sucessfuly."),
			@ApiResponse(responseCode = "404", description = "The enterprise doesn't exist."),
			@ApiResponse(responseCode = "500", description = "The enterprise can't be deleted. There are some Game(s) referencing the enterprise.")
	})
	public ResponseEntity<?> deleteEnterprise(@PathVariable Long id) {
		if (getService().delete(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
}
