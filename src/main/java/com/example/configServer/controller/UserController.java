package com.example.configServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.configServer.DTO.UserDto;
import com.example.configServer.entity.User;
import com.example.configServer.repository.UserRepository;
import com.example.configServer.service.UserService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(value = "main", description = "main controller")
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public UserDto saveMember(@RequestBody UserDto userdto) { 
		userService.signup(userdto);
		log.info("=======================save=======================");
		return userdto;
	}

	// 회원 조회
	@GetMapping(value = "/list") 
	public Iterable<User> getAllmembers() {
		return userRepository.findAll();
	}

	// 해당 email의 사용자를 반환
	@GetMapping(value = "/list/{email}")
	public User findOne(@PathVariable String email) {
		return userRepository.findByEmail(email);
	}

}
