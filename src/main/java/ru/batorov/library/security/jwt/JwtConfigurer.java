package ru.batorov.library.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.batorov.library.services.JwtBlackListService;
import ru.batorov.library.services.PeopleService;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtProvider jwtTokenProvider;
	private final PeopleService peopleService;
    private final JwtBlackListService jwtBlackListService;

	public JwtConfigurer(JwtProvider jwtTokenProvider, PeopleService peopleService, JwtBlackListService jwtBlackListService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.peopleService = peopleService;
        this.jwtBlackListService = jwtBlackListService;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(jwtTokenProvider, peopleService);
        //JwtBlackListFilter jwtBlackListFilter = new JwtBlackListFilter(jwtBlackListService);
        //httpSecurity.addFilterBefore(jwtBlackListFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
