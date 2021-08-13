package com.example.configServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.configServer.entity.User;
import com.example.configServer.security.JwtRequest;
import com.example.configServer.security.JwtResponse;
import com.example.configServer.service.JwtUserDetailsServiceImpl;
import com.example.configServer.util.CookieUtil;
import com.example.configServer.util.JwtTokenUtil;
import com.example.configServer.util.RedisUtil;

@RestController
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private CookieUtil cookieUtil;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private JwtUserDetailsServiceImpl userDetailService;

	@Cacheable(value = "#refreshToken", cacheManager = "redisCacheManager")
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletResponse response) throws Exception {
		// token : Header, Payload, Signature의 3 부분
		
		// 아이디와 패스워드로, Security 가 알아 볼 수 있는 token 객체로 변경한다.
		// AuthenticationManager 에 token 을 넘기면 UserDetailsService 가 받아 처리하도록 한다.
		// 실제 SecurityContext 에 authentication 정보를 등록한다.		

		UsernamePasswordAuthenticationToken token_ = new UsernamePasswordAuthenticationToken(authenticationRequest.getUserId(), authenticationRequest.getPassword());
		Authentication authentication =  authenticationManager.authenticate(token_);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		final UserDetails userDetail = userDetailService.loadUserByUsername(authenticationRequest.getUserId());
		final String token = jwtTokenUtil.generateToken(userDetail);
		final String rToken = jwtTokenUtil.generateRefreshToken(userDetail);
		Cookie accessToken = cookieUtil.createCookie(JwtTokenUtil.ACCESS_TOKEN_NAME, token);
		Cookie refreshToken = cookieUtil.createCookie(JwtTokenUtil.REFRESH_TOKEN_NAME, rToken);
		redisUtil.setDataExpire(rToken, authenticationRequest.getUserId(), JwtTokenUtil.REFRESH_TOKEN_VALIDITY);
		response.addCookie(accessToken);
		response.addCookie(refreshToken);

		return ResponseEntity.ok(new JwtResponse(token));
	}

}
