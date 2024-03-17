package ru.batorov.library.util;

import java.util.Collection;

import org.modelmapper.ModelMapper;

import ru.batorov.library.dto.RoleDTO;
import ru.batorov.library.dto.book.BookAdminDTO;
import ru.batorov.library.dto.book.BookDTOInterface;
import ru.batorov.library.dto.book.BookOwnerDTO;
import ru.batorov.library.dto.book.BookUserDTO;
import ru.batorov.library.dto.book.BookUsersInfoDTO;
import ru.batorov.library.dto.credentials.CredentialsAdminDTO;
import ru.batorov.library.dto.credentials.CredentialsUserDTO;
import ru.batorov.library.dto.credentials.PasswordDTO;
import ru.batorov.library.dto.person.PersonAdminDTO;
import ru.batorov.library.dto.person.PersonDTOInterface;
import ru.batorov.library.dto.person.PersonUserDTO;
import ru.batorov.library.dto.person.PersonUserRestDTO;
import ru.batorov.library.dto.person.PersonWithBooksAdminDTO;
import ru.batorov.library.dto.person.PersonWithBooksUserDTO;
import ru.batorov.library.dto.person.PersonRegistrationDTO;
import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.models.Role;

/**
 * Convert Entities to DTO and vice versa
 */
public class DTOConvert {
	private DTOConvert() {
		throw new AssertionError();
	}

	public static Person converToPerson(PersonDTOInterface personDTOInterface, ModelMapper modelMapper) {
		return modelMapper.map(personDTOInterface, Person.class);
	}

	public static PersonUserDTO convertToPersonUserDTO(Person person, ModelMapper modelMapper) {
		return modelMapper.map(person, PersonUserDTO.class);
	}
	
	public static PersonWithBooksUserDTO convertToPersonWithBooksUserDTO(Person person, ModelMapper modelMapper) {
		return modelMapper.map(person, PersonWithBooksUserDTO.class);
	}

	public static PersonAdminDTO convertToPersonAdminDTO(Person person, ModelMapper modelMapper) {
		return modelMapper.map(person, PersonAdminDTO.class);
	}
	
	public static PersonWithBooksAdminDTO convertToPersonWithBooksAdminDTO(Person person, ModelMapper modelMapper) {
		return modelMapper.map(person, PersonWithBooksAdminDTO.class);
	}

	public static Collection<PersonUserDTO> convertToPersonUserDTOCollection(Collection<Person> persons,
			ModelMapper modelMapper) {
		return persons.stream().map(person -> DTOConvert.convertToPersonUserDTO(person, modelMapper)).toList();
	}

	public static Collection<PersonAdminDTO> convertToPersonAdminDTOCollection(Collection<Person> persons,
			ModelMapper modelMapper) {
		return persons.stream().map(person -> DTOConvert.convertToPersonAdminDTO(person, modelMapper)).toList();
	}

	public static Person converToPerson(CredentialsUserDTO CredentialsUserDTO, ModelMapper modelMapper) {
		return modelMapper.map(CredentialsUserDTO, Person.class);
	}
	
	public static Person converToPerson(PasswordDTO passwordDTO, ModelMapper modelMapper) {
		Person person = new Person();
		person.setPassword(passwordDTO.getPassword());
		return person;
	}

	public static Person converToPerson(CredentialsAdminDTO credentialsAdminDTO, ModelMapper modelMapper) {
		return modelMapper.map(credentialsAdminDTO, Person.class);
	}

	public static CredentialsUserDTO convertToCredentialsUserDTO(Person person, ModelMapper modelMapper) {
		return modelMapper.map(person, CredentialsUserDTO.class);
	}

	public static CredentialsAdminDTO convertToCredentialsAdminDTO(Person person, ModelMapper modelMapper) {
		return modelMapper.map(person, CredentialsAdminDTO.class);
	}

	public static Book converToBook(BookDTOInterface bookDTOInterface, ModelMapper modelMapper) {
		return modelMapper.map(bookDTOInterface, Book.class);
	}
	
	public static BookUserDTO convertToBookUserDTO(Book book, ModelMapper modelMapper) {
		return modelMapper.map(book, BookUserDTO.class);
	}

	public static Collection<BookUserDTO> convertToBookUserDTOCollection(
			Collection<Book> books, ModelMapper modelMapper) {
		return books.stream().map(book -> DTOConvert.convertToBookUserDTO(book, modelMapper)).toList();
	}

	public static Collection<BookAdminDTO> convertToBookAdminDTOCollection(
			Collection<Book> books, ModelMapper modelMapper) {
		return books.stream().map(book -> DTOConvert.convertToBookAdminDTO(book, modelMapper)).toList();
	}

	public static BookUsersInfoDTO convertToBookUsersInfoDTO(Book book, ModelMapper modelMapper) {
		return modelMapper.map(book, BookUsersInfoDTO.class);
	}
	
	public static BookOwnerDTO convertToBookOwnerDTO(Book book, ModelMapper modelMapper) {
		return modelMapper.map(book, BookOwnerDTO.class);
	}

	public static BookAdminDTO convertToBookAdminDTO(Book book, ModelMapper modelMapper) {
		return modelMapper.map(book, BookAdminDTO.class);
	}

	public static Person converToPerson(Role role, ModelMapper modelMapper) {
		return modelMapper.map(role, Person.class);
	}
	
	public static Person converToPerson(RoleDTO roleDTO, ModelMapper modelMapper) {
		return modelMapper.map(roleDTO, Person.class);
	}
	
	public static Role convertToRole(RoleDTO roleDTO, ModelMapper modelMapper) {
		return modelMapper.map(roleDTO, Role.class);
	}

	public static RoleDTO convertToRoleDTO(Role role, ModelMapper modelMapper) {
		return modelMapper.map(role, RoleDTO.class);
	}
	public static Collection<RoleDTO> convertToRoleDTOCollection(Collection<Role> roles,
			ModelMapper modelMapper) {
		return roles.stream().map(role -> DTOConvert.convertToRoleDTO(role, modelMapper)).toList();
	}
}
