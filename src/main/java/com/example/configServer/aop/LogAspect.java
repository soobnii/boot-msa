package com.example.configServer.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

	@Before("execution(* com.example.configServer.controller.*Controller.*(..))")
    public void doSomethingBefore() {
		log.info("AOP Test : Before "); //@Slf4j
		LOGGER.info("=======!!!!!!!=================="); //LoggerFactory
    }

}
