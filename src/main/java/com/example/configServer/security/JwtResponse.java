package com.example.configServer.security;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -3441742282219406788L;
	private final String jwttoken;

	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}

	public String getToken() {
		return this.jwttoken;
	}

}