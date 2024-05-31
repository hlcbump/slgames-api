package com.slgames.store.model;

import java.time.LocalDate;

import com.slgames.store.dtos.enterprise.InsertEnterpriseDTO;
import com.slgames.store.dtos.enterprise.UpdateEnterpriseDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "enterprise")
@Table(name = "enterprises")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class Enterprise implements Cloneable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name_enterprise")
	private String name;
	@Column(name = "foundationDate")
	private LocalDate foundationDate;
	
	public Enterprise(Long id) {
		setId(id);
	}
	
	public Enterprise(InsertEnterpriseDTO enterpriseDto) {
		setName(enterpriseDto.name());
		setFoundationDate(enterpriseDto.foundationDate());
	}
	
	public void update(UpdateEnterpriseDTO enterpriseDto) {
		if (enterpriseDto.name() != null && !enterpriseDto.name().isBlank()) setName(enterpriseDto.name());
		if (enterpriseDto.foundationDate() != null) setFoundationDate(enterpriseDto.foundationDate());
	}

	@Override
	public Enterprise clone() throws CloneNotSupportedException {
		return (Enterprise) super.clone();
	}
	
	
}
