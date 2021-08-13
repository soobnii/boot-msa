package com.example.configServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.configServer.configClientVO.ConfigVO;

@RestController
@RefreshScope
public class ClientController {

	@Autowired
	private ConfigVO phase;

	@RequestMapping(value="/configClient")
	public ConfigVO test() {
		return phase;
	}

}