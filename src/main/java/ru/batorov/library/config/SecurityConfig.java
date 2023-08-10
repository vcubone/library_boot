package ru.batorov.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.batorov.library.services.CredentialsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	private final CredentialsService credentialsService;
	
	public SecurityConfig(CredentialsService credentialsService) {
		this.credentialsService = credentialsService;
	}


	// настраиваем аутентификацию
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(credentialsService).passwordEncoder(getPasswordEncoder());
	}

	// показывает как шифруются пароли
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// конфигурируем авторизацию
		//TODO добавить csrf исключение для rest
        http
                .authorizeHttpRequests()
                //.antMatchers("/people/{bookId}{bookId} {bookId}/", "/admin").hasRole("ADMIN")//ïðè hasrole ROLE_ îòáðàñûâàåòñÿ
                .antMatchers("/books/{bookId}/addowner", "/books/{bookId}/release", "/account/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/auth/register", "/auth/login", "/error", "/", "/books", "/books/{bookId}", "/books/search").permitAll()// смотрим какой запрос пришел в приложение и разрешаем туда заходить всем
                .anyRequest().hasAnyRole("ADMIN")//"USER",
                .and()// раньше настраивали авторизацию, дальше другой блок
                .formLogin(login -> login.loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")//показывает по какому url секьюрити будет ждать данные с формы
                        // аутентификации, если мы ее сами не делаем
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth/login?error"))
                .logout(logout -> logout.logoutUrl("/logout")//удаление сессии на сервере и кукис в браузере
                        .logoutSuccessUrl("/"))//переход при успехе
				;
	}
}

 