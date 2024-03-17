package ru.batorov.library.controllers.rest;

import java.util.Collection;
import javax.validation.Valid;

import static ru.batorov.library.util.AuthenticationHelper.*;

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
import ru.batorov.library.dto.book.BookUsersInfoDTO;
import ru.batorov.library.dto.credentials.PasswordDTO;
import ru.batorov.library.dto.person.PersonUserRestDTO;
import ru.batorov.library.dto.person.PersonWithBooksUserDTO;
import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.services.PeopleService;
import ru.batorov.library.util.exceptions.ErrorsGetter;
import springfox.documentation.annotations.ApiIgnore;

import static ru.batorov.library.util.DTOConvert.*;

@RestController
@RequestMapping("/api/account")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Account", description = "The Account API. Contains all the operations that can be performed with your account.")
public class AccountRestController {
	private final PeopleService peopleService;
	private final ModelMapper modelMapper;

	public AccountRestController(PeopleService peopleService,
			ModelMapper modelMapper) {
		this.peopleService = peopleService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/main")
	@Operation(summary = "Shows your account main information", tags = "Account", security = @SecurityRequirement(name = "Bearer Authentication"))
	public PersonWithBooksUserDTO userInfo(@ApiIgnore Authentication authentication) {
		Person person = peopleService.getPersonByUsername(getUsernameByAuthentication(authentication));
		PersonWithBooksUserDTO PersonWithBooksUserDTO = convertToPersonWithBooksUserDTO(person, modelMapper);

		Collection<Book> books = peopleService.findBooksByPersonId(person.getId());
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
		peopleService.update(getUsernameByAuthentication(authentication), person);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PatchMapping("/credentials/edit")
	@Operation(summary = "Updates your account credentials", tags = "Account", security = @SecurityRequirement(name = "Bearer Authentication"))
	@ApiResponse(responseCode = "400", description = "Bad input values")
	public ResponseEntity<HttpStatus> updateCredentials(
			@RequestBody @Valid PasswordDTO passwordDTO,
			@ApiIgnore BindingResult bindingResult, @ApiIgnore Authentication authentication) {
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(ErrorsGetter.getErrors(bindingResult));
		}
		Person person = converToPerson(passwordDTO, modelMapper);

		peopleService.update(getUsernameByAuthentication(authentication), person);
		return ResponseEntity.ok(HttpStatus.OK);
	}
}
