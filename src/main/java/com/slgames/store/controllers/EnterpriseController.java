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

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Getter;

@RestController
@RequestMapping("/enterprise")
@Getter
public class EnterpriseController {
	
	
	@Autowired
	private EnterpriseService service;
	
	@GetMapping
	public ResponseEntity<?> findAll(){
		return ResponseEntity.ok(getService().findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findEnterpriseById(@PathVariable Long id){
		return ResponseEntity.of(getService().findById(id));
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> insertEnterprise(@RequestBody @Valid InsertEnterpriseDTO enterpriseDto, UriComponentsBuilder builder ){
		Enterprise enterprise = getService().createEnterprise(enterpriseDto);
		var uri = builder.path("/enterprise/{id}").buildAndExpand(enterprise.getId()).toUri();
		return ResponseEntity.created(uri).body(EnterpriseDTOFactory.getDTO(enterprise, TypeDTO.CREATED));
	}
	
	@PutMapping
	@Transactional
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
	public ResponseEntity<?> deleteEnterprise(@PathVariable Long id) {
		if (getService().delete(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
}
