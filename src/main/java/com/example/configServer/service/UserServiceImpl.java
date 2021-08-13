package com.example.configServer.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.configServer.DTO.UserDto;
import com.example.configServer.entity.User;
import com.example.configServer.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired 
	private ModelMapper modelMapper;

	@Override
	public UserDto signup(UserDto userDto) {
		User user = userRepository.findByEmail(userDto.getEmail());
		if (user == null) {
			user = new User()
					.setUserId(userDto.getUserId())
					.setEmail(userDto.getEmail())
					.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()))
					.setFirstName(userDto.getFirstName())
					.setLastName(userDto.getLastName())
					.setMobileNumber(userDto.getMobileNumber());
			user = userRepository.save(user);
			log.debug("=======SAVE======" + user);
//			return UserMapper.toUserDto(userRepository.save(user));
			return modelMapper.map(user, UserDto.class);
		}
		return null;
	}

	@Override
	public UserDto findUserByEmail(String email) {
		Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
		// ModelMapper.map(엔티티클래스,도메인클래스리터럴)
		if (user.isPresent()) {
			return modelMapper.map(user.get(), UserDto.class);
		}
//		throw exception(USER, ENTITY_NOT_FOUND, email);
		return null;
	}
	
	@Override
	public UserDto findUserByUserId(String userId) {
		Optional<User> user = Optional.ofNullable(userRepository.findByUserId(userId));
		if (user.isPresent()) {
			return modelMapper.map(user.get(), UserDto.class);
		}
//		throw exception(USER, ENTITY_NOT_FOUND, email);
		return null;
	}

}
