package com.example.configServer.security;

import java.io.Serializable;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequest implements Serializable {

	private static final long serialVersionUID = -7210921419248359827L;
	private String userId;
	private String password;

	// need default constructor for JSON Parsing
	public JwtRequest() {

	}

	public JwtRequest(String userId, String password) {
		this.setUserId(userId);
		this.setPassword(password);
	}

}
