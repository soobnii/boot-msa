package com.example.configServer.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.configServer.service.JwtUserDetailsServiceImpl;
import com.example.configServer.util.CookieUtil;
import com.example.configServer.util.JwtTokenUtil;
import com.example.configServer.util.RedisUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUserDetailsServiceImpl jwtUserDetailService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private CookieUtil cookieUtil;

	@Autowired
	private RedisUtil redisUtil;

	private static final List<String> EXCLUDE_URL =
		Collections.unmodifiableList(
				Arrays.asList("/authenticate", "/v2/api-docs","/configuration/ui", "/swagger-resources", "/configuration/security",
						"/swagger-ui.html", "/webjars/**", "/swagger/**", "/swagger-resources/**"
		));

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		final Cookie jwtTokenCookie = cookieUtil.getCookie(request,JwtTokenUtil.ACCESS_TOKEN_NAME); 
		//final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;
		String refreshJwtoken = null;
		String refreshUname = null;

		try {
			// cookie 에서 token 조회
			if(jwtTokenCookie != null) {
				jwtToken = jwtTokenCookie.getValue();
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			}
			//토큰 유효성 검사 
			if(username != null) { // && SecurityContextHolder.getContext().getAuthentication() == null
				UserDetails userDetails = this.jwtUserDetailService.loadUserByUsername(username);
				if(jwtTokenUtil.validateToken(jwtToken, userDetails)) {
					// 아이디와 권한으로, Security 가 알아 볼 수 있는 token 객체로 변경한다.
					// Records the remote address and will also set the session Id if a session already exists (it won't create one).
					// 실제 SecurityContext 에 authentication 정보를 등록한다.
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null ,userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		} catch (ExpiredJwtException e) {
			// token 만료 시 refreshtoken 으로 token 재발급
			Cookie refreshToken = cookieUtil.getCookie(request,JwtTokenUtil.REFRESH_TOKEN_NAME);
			if(refreshToken!=null) {
				refreshJwtoken = refreshToken.getValue();
			}
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException ::::: Unable to get JWT Token");
		} catch(Exception e) {
			logger.error("ERROR ::: " + e.getMessage());
		}

		// refreshtoken 으로 token 재발급
		try{
			if(refreshJwtoken != null) {
				refreshUname = redisUtil.getData(refreshJwtoken);
				if(refreshUname.equals(jwtTokenUtil.getUsernameFromToken(refreshJwtoken))) {
					UserDetails userDetails = jwtUserDetailService.loadUserByUsername(refreshUname);
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					String newToken =jwtTokenUtil.generateToken(userDetails);
					Cookie newAccessToken = cookieUtil.createCookie(JwtTokenUtil.ACCESS_TOKEN_NAME,newToken);
					response.addCookie(newAccessToken);
				}
			}
		} catch(Exception e) {
			logger.error("ERROR ::: " + e.getMessage());
		}

//		//헤더에서 Authorization > token 값을 꺼내어 해당 유저가 실제 DB에 있는지 파악
//		if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//			jwtToken = requestTokenHeader.substring(7); // 토큰에서 'Bearer ' 제거
//			try {
//				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//			} catch (IllegalArgumentException e) {
//				System.out.println("Unable to get JWT Token");
//			} catch (ExpiredJwtException e) {
//				System.out.println("JWT Token has expired");
//			}
//		} else {
//			logger.warn("JWT Token does not begin with Bearer String");
//		}

		filterChain.doFilter(request,response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
	}

}
