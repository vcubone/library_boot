package ru.batorov.library.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.batorov.library.dto.RegistrationDTO;
import ru.batorov.library.models.Credentials;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.util.CredentialsValidator;
import ru.batorov.library.util.PersonValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {
	private final PersonValidator personValidator;
	private final PeopleService peopleService;
	private final CredentialsValidator credentialsValidator;
	private final ModelMapper modelMapper;

	public AuthController(PersonValidator personValidator, PeopleService peopleService,
			CredentialsValidator credentialsValidator, ModelMapper modelMapper) {
		this.personValidator = personValidator;
		this.peopleService = peopleService;
		this.credentialsValidator = credentialsValidator;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/login")
	public String loginPage() {
		return "auth/login";
	}

	@GetMapping("/register")
	public String registraionPage(@ModelAttribute("registrationDTO") RegistrationDTO registrationDTO) {
		System.out.println("registrPage");
		return "auth/register";
	}
	
	@PostMapping("/register")
	public String performRegistration(@ModelAttribute("registrationDTO") @Valid RegistrationDTO registrationDTO, BindingResult bindingResult) {
		Person person = modelMapper.map(registrationDTO, Person.class);
		Credentials credentials = modelMapper.map(registrationDTO, Credentials.class);
		
		personValidator.validate(person, bindingResult);
		credentialsValidator.validate(credentials, bindingResult);
		
		if (bindingResult.hasErrors())
			return "/auth/register";
		credentials.setPerson(person);
		person.setCredentials(credentials);
		
		peopleService.register(person);
		return "redirect:/auth/login";
	}
}
