package ru.batorov.library.controllers;

import static ru.batorov.library.util.AuthenticationHelper.getUserIdByAuthentication;
import static ru.batorov.library.util.AuthenticationHelper.getPersonFromAuthentication;

import java.util.Collection;

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
import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.util.PersonsCredentialsValidator;

import static ru.batorov.library.util.DTOConvert.*;

@Controller
@RequestMapping("/account")
public class AccauntController {
	private final PeopleService peopleService;
	private final PersonsCredentialsValidator credentialsValidator;
	private final ModelMapper modelMapper;

	public AccauntController(PeopleService peopleService, PersonsCredentialsValidator credentialsValidator,
			ModelMapper modelMapper) {
		this.peopleService = peopleService;
		this.credentialsValidator = credentialsValidator;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/main")
	public String userInfo(Model model, Authentication authentication) {
		Person person = peopleService.show(getUserIdByAuthentication(authentication));
		Collection<Book> books = peopleService.getBooksByPersonId(person.getId());
		Collection<BookUsersInfoDTO> bookUsersInfoDTOs = books.stream()
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
		if (bindingResult.hasErrors())
			return "account/edit";

		peopleService.update(getUserIdByAuthentication(authentication), person);
		return "redirect:/account/main";
	}

	@GetMapping("/credentials/edit")
	public String editCredentials(Model model, Authentication authentication) {
		Person person = peopleService.show(getUserIdByAuthentication(authentication));
		person.setPassword(null);
		model.addAttribute("credentialsUserDTO", convertToCredentialsUserDTO(person, modelMapper));
		return "account/credentials/edit";
	}

	@PatchMapping("/credentials/edit")
	public String updateCredentials(@ModelAttribute("credentialsUserDTO") @Valid CredentialsUserDTO credentialsUserDTO,
			BindingResult bindingResult, Authentication authentication) {
		Person person = converToPerson(credentialsUserDTO, modelMapper);
		person.setId(getUserIdByAuthentication(authentication));
		credentialsValidator.validate(person, bindingResult);
		if (bindingResult.hasErrors())
			return "account/credentials/edit";

		peopleService.update(getUserIdByAuthentication(authentication), person);
		return "redirect:/account/main";
	}
}
