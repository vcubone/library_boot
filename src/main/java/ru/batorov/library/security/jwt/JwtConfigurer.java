package ru.batorov.library.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.batorov.library.services.PeopleService;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtProvider jwtTokenProvider;
	private final PeopleService peopleService;

    public JwtConfigurer(JwtProvider jwtTokenProvider, PeopleService peopleService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.peopleService = peopleService;
	}

	@Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(jwtTokenProvider, peopleService);
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
