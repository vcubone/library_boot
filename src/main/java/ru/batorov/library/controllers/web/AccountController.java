package ru.batorov.library.controllers.web;

import static ru.batorov.library.util.AuthenticationHelper.*;

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

import ru.batorov.library.dto.book.BookUsersInfoDTO;
import ru.batorov.library.dto.credentials.CredentialsUserDTO;
import ru.batorov.library.dto.credentials.PasswordDTO;
import ru.batorov.library.dto.person.PersonUserDTO;
import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.services.SessionService;
import static ru.batorov.library.util.DTOConvert.*;

@Controller
@RequestMapping("/account")
public class AccountController {
	private final PeopleService peopleService;
	private final ModelMapper modelMapper;
	private final SessionService sessionService;

	public AccountController(PeopleService peopleService, ModelMapper modelMapper, SessionService sessionService) {
		this.peopleService = peopleService;
		this.modelMapper = modelMapper;
		this.sessionService = sessionService;
	}

	@GetMapping("/main")
	public String userInfo(Model model, Authentication authentication) {
		Person person = peopleService.getPersonById(getUserIdByAuthentication(authentication));
		Collection<Book> books = peopleService.findBooksByPersonId(person.getId());
		Collection<BookUsersInfoDTO> bookUsersInfoDTOs = books.stream()
				.map(book -> convertToBookUsersInfoDTO(book, modelMapper)).toList();
		model.addAttribute("personUserDTO", convertToPersonUserDTO(person, modelMapper));
		model.addAttribute("bookUsersInfoDTOs", bookUsersInfoDTOs);

		return "account/main";
	}

	@GetMapping("/edit")
	public String edit(Model model, Authentication authentication) {
		Person person = peopleService.getPersonById(getUserIdByAuthentication(authentication));
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
		model.addAttribute("credentialsUserDTO", new CredentialsUserDTO());
		return "account/credentials/edit";
	}

	@PatchMapping("/credentials/edit")
	public String updateCredentials(@ModelAttribute("credentialsUserDTO") @Valid PasswordDTO passwordDTO,
			BindingResult bindingResult, Authentication authentication) {
		if (bindingResult.hasErrors())
			return "account/credentials/edit";
		Person person = converToPerson(passwordDTO, modelMapper);

		peopleService.update(getUsernameByAuthentication(authentication), person);
		sessionService.expireUserSessions(getUsernameByAuthentication(authentication));
		return "redirect:/account/main";
	}
}
