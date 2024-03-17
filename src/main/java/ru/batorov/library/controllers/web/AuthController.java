package ru.batorov.library.controllers.web;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.batorov.library.dto.person.PersonRegistrationDTO;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.util.UsernameValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {
	private final PeopleService peopleService;
	private final UsernameValidator usernameValidator;
	private final ModelMapper modelMapper;

	public AuthController(PeopleService peopleService,
			UsernameValidator usernameValidator, ModelMapper modelMapper) {
		this.peopleService = peopleService;
		this.usernameValidator = usernameValidator;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/login")
	public String loginPage() {
		return "auth/login";
	}

	@GetMapping("/register")
	public String registraionPage(@ModelAttribute("registrationDTO") PersonRegistrationDTO registrationDTO) {
		System.out.println("registrPage");
		return "auth/register";
	}

	@PostMapping("/register")
	public String performRegistration(@ModelAttribute("registrationDTO") @Valid PersonRegistrationDTO registrationDTO,
			BindingResult bindingResult) {
		Person person = modelMapper.map(registrationDTO, Person.class);

		usernameValidator.validate(person, bindingResult);

		if (bindingResult.hasErrors())
			return "/auth/register";

		peopleService.register(person);
		return "redirect:/auth/login";
	}
}
