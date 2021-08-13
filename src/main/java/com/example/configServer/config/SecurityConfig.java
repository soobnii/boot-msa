package com.example.configServer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.configServer.security.JwtAuthenticationEntryPoint;
import com.example.configServer.security.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Controller에서 특정 페이지에 특정 권한이 있는 유저만 접근을 허용할 경우 @PreAuthorize 어노테이션을 사용하기 위함. (필수X)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	private static final String[] PUBLIC_URI = {
		"/authenticate", "/v2/api-docs","/configuration/ui", "/swagger-resources", "/configuration/security",
		"/swagger-ui.html", "/webjars/**", "/swagger/**", "/swagger-resources/**","/user/save"
	};

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.jdbcAuthentication().dataSource(dataSource);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// 개발 편의성을 위해 CSRF 프로텍션을 비활성화
			.csrf().disable()
			// HTTP 기본 인증 비활성화
			.httpBasic().disable()
			// 폼 기반 인증 비활성화  
			.formLogin().disable()
			// stateless한 세션 정책 설정  (Spring Security에서 Session을 생성하거나 사용하지 않도록 설정)
			// SecurityContextHolder 사용을 위해선 ALWAYS 로 변경해야 함
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			// 리소스 별 허용 범위 설정  
			.authorizeRequests()
				.antMatchers(PUBLIC_URI).permitAll()
				.anyRequest().authenticated()
			.and()
			// 인증 오류 발생 시 처리를 위한 핸들러 추가  
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
		;

		// 토큰 인증 필터 추가
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers(PUBLIC_URI);
//	}

}
