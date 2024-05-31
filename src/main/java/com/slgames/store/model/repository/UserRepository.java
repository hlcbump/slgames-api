package com.slgames.store.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slgames.store.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
