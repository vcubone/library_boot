package ru.batorov.library.controllers;

import static ru.batorov.library.util.AuthenticationHelper.getUserIdByAuthentication;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.batorov.library.dto.BookUsersInfoDTO;
import ru.batorov.library.dto.CredentialsUserDTO;
import ru.batorov.library.dto.PersonUserDTO;
import ru.batorov.library.models.Book;
import ru.batorov.library.models.Credentials;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.CredentialsService;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.util.CredentialsValidator;
import ru.batorov.library.util.PersonValidator;

import static ru.batorov.library.util.DTOConvert.*;

@Controller
@RequestMapping("/account")
public class AccauntController {
	private final PeopleService peopleService;
	private final PersonValidator personValidator;
	private final CredentialsValidator credentialsValidator;
	private final CredentialsService credentialsService;
	private final ModelMapper modelMapper;

	public AccauntController(PeopleService peopleService, PersonValidator personValidator,
			CredentialsValidator credentialsValidator, CredentialsService credentialsService, ModelMapper modelMapper) {
		this.peopleService = peopleService;
		this.personValidator = personValidator;
		this.credentialsValidator = credentialsValidator;
		this.credentialsService = credentialsService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/main")
	public String userInfo(Model model, Authentication authentication) {
		Person person = peopleService.show(getUserIdByAuthentication(authentication));
		List<Book> books = peopleService.getBooksByPersonId(person.getPersonId());
		List<BookUsersInfoDTO> bookUsersInfoDTOs = books.stream()
				.map(book -> convertToBookUsersInfoDTO(book, modelMapper)).toList();
		model.addAttribute("personUserDTO", convertToPersonUserDTO(person, modelMapper));
		model.addAttribute("bookUsersInfoDTOs", bookUsersInfoDTOs);

		return "account/main";
	}

	@GetMapping("/edit")
	public String edit(Model model, Authentication authentication) {
		Person person = peopleService.show(getUserIdByAuthentication(authentication));
		model.addAttribute("personUserDTO", convertToPersonUserDTO(person, modelMapper));
		return "account/edit";
	}

	@PatchMapping("/edit")
	public String update(@ModelAttribute("personUserDTO") @Valid PersonUserDTO personUserDTO,
			BindingResult bindingResult,
			Authentication authentication) {
		Person person = converToPerson(personUserDTO, modelMapper);
		personValidator.validate(person, bindingResult);
		if (bindingResult.hasErrors())
			return "account/edit";

		peopleService.update(getUserIdByAuthentication(authentication), person);
		return "redirect:/account/main";
	}

	@GetMapping("/credentials/edit")
	public String editCredentials(Model model, Authentication authentication) {
		Credentials credentials = credentialsService.show(getUserIdByAuthentication(authentication));
		model.addAttribute("credentialsUserDTO", convertToCredentialsUserDTO(credentials, modelMapper));
		return "account/credentials/edit";
	}

	@PatchMapping("/credentials/edit")
	public String updateCredentials(@ModelAttribute("credentialsUserDTO") @Valid CredentialsUserDTO credentialsUserDTO,
			BindingResult bindingResult, Authentication authentication) {
		Credentials credentials = converToCredentials(credentialsUserDTO, modelMapper);
		credentialsValidator.validate(credentials, bindingResult);
		if (bindingResult.hasErrors())
			return "account/credentials/edit";

		credentialsService.update(getUserIdByAuthentication(authentication), credentials);
		return "redirect:/account/main";
	}
}
