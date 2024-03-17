package ru.batorov.library.config;

import java.util.stream.Stream;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import ru.batorov.library.security.AuthenticationCheckFilter;
import ru.batorov.library.security.jwt.JwtFilter;
import ru.batorov.library.security.jwt.JwtProvider;
import ru.batorov.library.services.PeopleService;

@EnableWebSecurity
public class SecurityConfig {
	private static final String[] SWAGGER_WHITELIST = {
			// -- Swagger UI v2
			"/v2/api-docs",
			"/swagger-resources",
			"/swagger-resources/**",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-ui.html",
			"/webjars/**",
			// -- Swagger UI v3 (OpenAPI)
			"/v3/api-docs/**",
			"/swagger-ui/**"
	};
	private static final String[] USER_WHITELIST = {
			"/books/\\d+/addowner",
			"/books/\\d+/release",
			"/account/.+"
	};
	private static final String[] ALL_WHITELIST = {
			"/auth/register",
			"/auth/login",
			"/error",
			"/",
			"/books",
			"/books/search"
	};
	private static final String[] ALL_API_WHITELIST = Stream.of(ALL_WHITELIST).map(str -> "/api" + str)
			.toArray(String[]::new);
	private static final String[] USER_API_WHITELIST = Stream.of(USER_WHITELIST).map(str -> "/api" + str)
			.toArray(String[]::new);

	private final PeopleService peopleService;
	private final JwtProvider jwtTokenProvider;

	public SecurityConfig(PeopleService peopleService, JwtProvider jwtTokenProvider) {
		this.peopleService = peopleService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	/*
	 * замена 
	 * нет .userDetailsService и .passwordEncoder норм?
	 * @Override
	 * protected void configure(AuthenticationManagerBuilder auth) throws Exception
	 * {
	 * auth.userDetailsService(peopleService).passwordEncoder(getPasswordEncoder());
	 * }
	 */

	@Bean
	@Order(1)
	public SecurityFilterChain ApiFilterChain(HttpSecurity http) throws Exception {

		http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
						.sessionFixation().newSession())
				.antMatcher("/api/**") // <= Security only available for /api/**
				.authorizeHttpRequests()
				.regexMatchers("/api/books/-?\\d+").permitAll()
				.antMatchers(ALL_API_WHITELIST).permitAll()
				.regexMatchers(USER_API_WHITELIST).hasRole("USER")
				.anyRequest().hasAnyRole("ADMIN")
				.and()
				.addFilterBefore(new JwtFilter(jwtTokenProvider, peopleService),
						UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain WebFilterChain(HttpSecurity http) throws Exception {
		http
				.sessionManagement(
						management -> management.maximumSessions(2).sessionRegistry(getSessionRegistry()))
				.authorizeHttpRequests()
				.antMatchers(SWAGGER_WHITELIST).permitAll()
				.regexMatchers("/books/-?\\d+").permitAll()
				.antMatchers(ALL_WHITELIST).permitAll()
				.regexMatchers(USER_WHITELIST).hasRole("USER")
				.anyRequest().hasAnyRole("ADMIN")
				.and()
				.formLogin(login -> login.loginPage("/auth/login")
						.loginProcessingUrl("/process_login")
						.defaultSuccessUrl("/", true)
						.failureUrl("/auth/login?error"))
				.logout(logout -> logout.logoutUrl("/logout")
						.logoutSuccessUrl("/"))
				.addFilterBefore(new AuthenticationCheckFilter(peopleService),
						UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	// показывает как шифруются пароли
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SessionRegistry getSessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisherl() {
		return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
	}
}
