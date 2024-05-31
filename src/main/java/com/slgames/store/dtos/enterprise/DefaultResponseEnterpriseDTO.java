package com.slgames.store.dtos.enterprise;

import java.time.LocalDate;

import com.slgames.store.dtos.DefaultDTO;
import com.slgames.store.model.Enterprise;

public record DefaultResponseEnterpriseDTO(
		Long id, 
		String name, 
		LocalDate foundationDate) implements DefaultDTO{
	
	public DefaultResponseEnterpriseDTO(Enterprise enterprise) {
		this(enterprise.getId(), enterprise.getName(), enterprise.getFoundationDate());
	}
}
