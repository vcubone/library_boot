package ru.batorov.library.controllers.rest;

import java.util.Collection;
import javax.validation.Valid;

import static ru.batorov.library.util.AuthenticationHelper.getUserIdByAuthentication;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.batorov.library.dto.BookUsersInfoDTO;
import ru.batorov.library.dto.CredentialsUserDTO;
import ru.batorov.library.dto.PersonWithBooksUserDTO;
import ru.batorov.library.dto.PersonUserRestDTO;
import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.util.ErrorsGetter;
import ru.batorov.library.util.PersonsCredentialsValidator;
import springfox.documentation.annotations.ApiIgnore;

import static ru.batorov.library.util.DTOConvert.*;
@RestController
@RequestMapping("/api/account")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Account", description = "The Account API. Contains all the operations that can be performed with your account.")
public class AccountRestController {
	private final PeopleService peopleService;
	private final PersonsCredentialsValidator credentialsValidator;
	private final ModelMapper modelMapper;
	

	public AccountRestController(PeopleService peopleService, PersonsCredentialsValidator credentialsValidator,
			ModelMapper modelMapper) {
		this.peopleService = peopleService;
		this.credentialsValidator = credentialsValidator;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/main")
	@Operation(summary = "Shows your account main information", tags = "Account", security = @SecurityRequirement(name = "Bearer Authentication"))
	public PersonWithBooksUserDTO userInfo(@ApiIgnore Authentication authentication) {
		Person person = peopleService.show(getUserIdByAuthentication(authentication));
		PersonWithBooksUserDTO PersonWithBooksUserDTO = convertToPersonWithBooksUserDTO(person, modelMapper);
		
		Collection<Book> books = peopleService.getBooksByPersonId(person.getId());
		Collection<BookUsersInfoDTO> bookUsersInfoDTOs = books.stream()
				.map(book -> convertToBookUsersInfoDTO(book, modelMapper)).toList();

		PersonWithBooksUserDTO.setBooks(bookUsersInfoDTOs);
		return PersonWithBooksUserDTO;
	}

	@PatchMapping("/edit")
	@Operation(summary = "Updates your account information", tags = "Account", security = @SecurityRequirement(name = "Bearer Authentication"))
	@ApiResponse(responseCode = "400", description = "Bad input values")
	public ResponseEntity<HttpStatus> update(
			@RequestBody @Valid PersonUserRestDTO personUserRestDTO,
			@ApiIgnore BindingResult bindingResult,
			@ApiIgnore Authentication authentication) {
		Person person = converToPerson(personUserRestDTO, modelMapper);
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(ErrorsGetter.getErrors(bindingResult));
		}
		peopleService.update(getUserIdByAuthentication(authentication), person);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@PatchMapping("/credentials/edit")
	@Operation(summary = "Updates your account credentials", tags = "Account", security = @SecurityRequirement(name = "Bearer Authentication"))
	@ApiResponse(responseCode = "400", description = "Bad input values")
	public ResponseEntity<HttpStatus> updateCredentials(
			@RequestBody @Valid CredentialsUserDTO credentialsUserDTO,
			@ApiIgnore BindingResult bindingResult, @ApiIgnore Authentication authentication) {
		Person person = converToPerson(credentialsUserDTO, modelMapper);
		person.setId(getUserIdByAuthentication(authentication));
		credentialsValidator.validate(person, bindingResult);
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(ErrorsGetter.getErrors(bindingResult));
		}
		
		peopleService.update(getUserIdByAuthentication(authentication), person);
		return ResponseEntity.ok(HttpStatus.OK);
	}
}
