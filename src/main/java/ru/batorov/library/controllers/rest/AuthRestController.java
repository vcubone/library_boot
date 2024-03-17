package ru.batorov.library.controllers.rest;

import static ru.batorov.library.util.DTOConvert.converToPerson;

import java.util.Collections;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.batorov.library.dto.credentials.CredentialsUserDTO;
import ru.batorov.library.dto.person.PersonRegistrationDTO;
import ru.batorov.library.models.Person;
import ru.batorov.library.security.jwt.JwtProvider;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.util.UsernameValidator;
import ru.batorov.library.util.exceptions.ErrorsGetter;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "The Auth API. Contains all the operations that can be performed to authenticate.")
public class AuthRestController {
	private final AuthenticationManager authenticationManager;
	private final PeopleService peopleService;
	private final JwtProvider jwtProvider;
	private final ModelMapper modelMapper;
	private final UsernameValidator personsCredentialsValidator;

	public AuthRestController(AuthenticationManager authenticationManager,
			PeopleService peopleService, JwtProvider jwtProvider, ModelMapper modelMapper,
			UsernameValidator personsCredentialsValidator) {
		this.authenticationManager = authenticationManager;
		this.peopleService = peopleService;
		this.jwtProvider = jwtProvider;
		this.modelMapper = modelMapper;
		this.personsCredentialsValidator = personsCredentialsValidator;
	}

	@PostMapping("/login")
	@Operation(summary = "Login page", description = "Returns jwt token like: \"jwt-token\" : {jwt token}", tags = "Auth")
	public Map<String, String> performLogin(@RequestBody @Valid CredentialsUserDTO credentialsUserDTO){
		UsernamePasswordAuthenticationToken authImputToken = //стандартная обертка логина и пароля в Спринг Сек
		new UsernamePasswordAuthenticationToken(credentialsUserDTO.getUsername(), credentialsUserDTO.getPassword());
		
		try {
			authenticationManager.authenticate(authImputToken);
		} catch (BadCredentialsException e) {//неправильные логин пароль
			throw e;
		}
		
		String token = jwtProvider.generateToken(credentialsUserDTO.getUsername());
		return Map.of("jwt-token", token);
	}
	
	@PostMapping("/register")
	@Operation(summary = "Register page", description = "Returns jwt token like: \"jwt-token\" : {jwt token}", tags = "Auth")
	public Map<String, String> performRegistration(@RequestBody @Valid PersonRegistrationDTO registrationDTO,
			@ApiIgnore BindingResult bindingResult) {
		Person person = converToPerson(registrationDTO, modelMapper);

		personsCredentialsValidator.validate(person, bindingResult);
		if (bindingResult.hasErrors())
			throw new IllegalArgumentException(ErrorsGetter.getErrors(bindingResult));

		peopleService.register(person);
		
		String token = jwtProvider.generateToken(person.getUsername());
		return Collections.singletonMap("jwt-token", token);
	}
}
