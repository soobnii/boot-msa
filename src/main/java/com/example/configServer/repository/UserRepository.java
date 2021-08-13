package com.example.configServer.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.configServer.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{

	User findByEmail(String email);
	User findByUserId(String userId);

}
