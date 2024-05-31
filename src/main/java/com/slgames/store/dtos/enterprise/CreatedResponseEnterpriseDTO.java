package com.slgames.store.dtos.enterprise;

import com.slgames.store.dtos.CreatedDTO;
import com.slgames.store.model.Enterprise;

public record CreatedResponseEnterpriseDTO(Long id, String name) implements CreatedDTO {
	
	public CreatedResponseEnterpriseDTO(Enterprise enterprise) {
		this(enterprise.getId(), enterprise.getName());
	}
}
