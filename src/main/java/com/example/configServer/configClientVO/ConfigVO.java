package com.example.configServer.configClientVO;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("example")
@Getter 
@Setter
public class ConfigVO {

	private String phase;

}
