package ru.batorov.library.controllers.web;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.batorov.library.dto.RegistrationDTO;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.util.PersonsCredentialsValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {
	private final PeopleService peopleService;
	private final PersonsCredentialsValidator personCredentialsValidator;
	private final ModelMapper modelMapper;

	public AuthController(PeopleService peopleService,
			PersonsCredentialsValidator credentialsValidator, ModelMapper modelMapper) {
		this.peopleService = peopleService;
		this.personCredentialsValidator = credentialsValidator;
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
	public String performRegistration(@ModelAttribute("registrationDTO") @Valid RegistrationDTO registrationDTO,
			BindingResult bindingResult) {
		Person person = modelMapper.map(registrationDTO, Person.class);

		personCredentialsValidator.validate(person, bindingResult);

		if (bindingResult.hasErrors())
			return "/auth/register";

		peopleService.register(person);
		return "redirect:/auth/login";
	}
}
