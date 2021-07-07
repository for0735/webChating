package com.eBook.mgr.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.eBook.mgr.common.security.CustomAccessDeniedHandler;
import com.eBook.mgr.common.security.CustomLoginSuccessHandler;
import com.eBook.mgr.common.security.CustomUserDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("security config......");
		
		
		http.formLogin()
		.loginPage("/auth/login")
		.loginProcessingUrl("/login")
		.successHandler(createAuthenticationSuccessHandler());
		
		http.logout()
		.logoutUrl("/auth/logout")
		.invalidateHttpSession(true);
		
		http.exceptionHandling()
		.accessDeniedHandler(createAccessDeniedHandler());
		
		http.rememberMe()
		.key("com.eBook.mgr")
		.tokenRepository(createJDBCRepository())
		.tokenValiditySeconds(60*60*24);
		
		// 접근 제한 설정
		http.authorizeRequests()
		.antMatchers("/")
		.authenticated()
		.antMatchers("/user/**").authenticated() 
		.antMatchers("/ebook/**").authenticated()
		.antMatchers("/board/**").authenticated();
	}

	@Bean
	public PersistentTokenRepository createJDBCRepository() {
		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);
		return repo;
	}

	@Bean
	public AccessDeniedHandler createAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public AuthenticationSuccessHandler createAuthenticationSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(createUserDetailsService())
		.passwordEncoder(createPasswordEncoder());
	}

	@Bean
	protected PasswordEncoder createPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService createUserDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	MappingJackson2JsonView jsonView(){
		return new MappingJackson2JsonView();
	}

}
