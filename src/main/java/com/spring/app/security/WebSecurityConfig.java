package com.spring.app.security;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.spring.app.model.User;
import com.spring.app.repository.UserRepository;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired 
	private UserDetailsService customUserDetailsService;
	
	@Autowired
	private UserRepository userRepository;
	
	
//	@Bean
//	public SecurityWebFilterChain http(ServerHttpSecurity http) throws Exception {
//	    DelegatingServerLogoutHandler logoutHandler = new DelegatingServerLogoutHandler(
//	            new WebSessionServerLogoutHandler(), new SecurityContextServerLogoutHandler()
//	    );
//
//	    http
//	        .authorizeExchange((exchange) -> exchange.anyExchange().authenticated())
//	        .logout((logout) -> logout.logoutHandler(logoutHandler));
//	    return http.build();
//	}
//	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
		.authorizeHttpRequests(authorize -> authorize
			.requestMatchers("/signup").permitAll()
			.anyRequest().authenticated()
		)
		.formLogin(form -> form
				.loginPage("/login")
				.usernameParameter("email")
				.defaultSuccessUrl("/friendList", true)
				.permitAll())
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login").deleteCookies("JSESSIONID")
		.invalidateHttpSession(true);


		
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	
	public void configure(AuthenticationManagerBuilder auth) {
		
		
		try {
			auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
			
			auth.eraseCredentials(false);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public User getCurrentlyLoggedUser() {
		
		return userRepository.findUserByName(getAuthentication().getName());
		
	}
	
	public Authentication getAuthentication() {
		
		return SecurityContextHolder.getContext().getAuthentication();
		
	}
	
	public String encodePassword(String password) {
		
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        
        return encoder.encode(password);
		
	}
	
	
    
}