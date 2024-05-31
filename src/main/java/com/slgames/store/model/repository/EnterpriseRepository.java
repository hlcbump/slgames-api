package com.slgames.store.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slgames.store.model.Enterprise;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Long>{

	public boolean existsByName(String name);

}
