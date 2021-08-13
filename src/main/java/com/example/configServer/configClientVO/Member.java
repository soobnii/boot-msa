package com.example.configServer.configClientVO;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

	@ApiParam(value = "member ID", required = true)
	private String id;

	@ApiParam(value = "member age", required = true)
	private int age;

	@ApiParam(value = "member email", required = true)
	private String email;

}
