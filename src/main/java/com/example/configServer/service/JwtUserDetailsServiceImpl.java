package com.example.configServer.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.configServer.DTO.UserDto;
import com.example.configServer.entity.User;
import com.example.configServer.repository.UserRepository;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	// userId로 회원을 찾아서 결과적으로 User 객체를 반환하는 역할
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		UserDto userDto = userService.findUserByUserId(userId);

		if(userDto != null) {
//			List<GrantedAuthority> authorities = getUserAuthority(userDto.getRoles());
//			return buildUserForAuthentication(userDto, authorities);
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			if("admin".equals(userDto.getUserId())) {
				grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
			} else {
				grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
			}
			return new org.springframework.security.core.userdetails.User(userDto.getUserId(), userDto.getPassword(), grantedAuthorities);
		} else {
			throw new UsernameNotFoundException("User not found with userId: " + userId);
		}

	}

//	public Member authenticateByEmailAndPassword(String email, String password) {
//		Member member = memberRepository.findByEmail(email)
//				.orElseThrow(() -> new UsernameNotFoundException(email));
//
//		if(!passwordEncoder.matches(password, member.getPassword())) {
//			throw new BadCredentialsException("Password not matched");
//		}
//
//		return member;
//	}

}
