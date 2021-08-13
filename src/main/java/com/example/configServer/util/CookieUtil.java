package com.example.configServer.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

	public Cookie createCookie(String cookieName, String value){
		Cookie token = new Cookie(cookieName,value);
		token.setHttpOnly(true);
		token.setMaxAge((int)JwtTokenUtil.JWT_TOKEN_VALIDITY);
		token.setPath("/");
		return token;
	}

	public Cookie getCookie(HttpServletRequest req, String cookieName){
		final Cookie[] cookies = req.getCookies();
		if(cookies==null) return null;
		for(Cookie cookie : cookies){
			if(cookie.getName().equals(cookieName))
				return cookie;
		}
		return null;
	}

}
