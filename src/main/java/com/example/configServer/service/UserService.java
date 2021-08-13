package com.example.configServer.service;

import com.example.configServer.DTO.UserDto;

public interface  UserService {

	UserDto signup(UserDto userDto);
	UserDto findUserByEmail(String email);
	UserDto findUserByUserId(String userId);

}
