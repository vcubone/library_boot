package ru.batorov.library.util;

import java.util.Collection;

import org.modelmapper.ModelMapper;

import ru.batorov.library.dto.BookAdminDTO;
import ru.batorov.library.dto.BookUserDTO;
import ru.batorov.library.dto.BookUsersInfoDTO;
import ru.batorov.library.dto.CredentialsAdminDTO;
import ru.batorov.library.dto.CredentialsUserDTO;
import ru.batorov.library.dto.PersonAdminDTO;
import ru.batorov.library.dto.PersonUserDTO;
import ru.batorov.library.dto.RoleDTO;
import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.models.Role;

public class DTOConvert {
	private DTOConvert() {
		throw new AssertionError();
	}

	public static Person converToPerson(PersonUserDTO personDTO, ModelMapper modelMapper) {
		return modelMapper.map(personDTO, Person.class);
	}

	public static Person converToPerson(PersonAdminDTO personAdminDTO, ModelMapper modelMapper) {
		return modelMapper.map(personAdminDTO, Person.class);
	}

	public static PersonUserDTO convertToPersonUserDTO(Person person, ModelMapper modelMapper) {
		return modelMapper.map(person, PersonUserDTO.class);
	}

	public static PersonAdminDTO convertToPersonAdminDTO(Person person, ModelMapper modelMapper) {
		return modelMapper.map(person, PersonAdminDTO.class);
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

	public static Person converToPerson(CredentialsAdminDTO credentialsAdminDTO, ModelMapper modelMapper) {
		return modelMapper.map(credentialsAdminDTO, Person.class);
	}

	public static CredentialsUserDTO convertToCredentialsUserDTO(Person person, ModelMapper modelMapper) {
		return modelMapper.map(person, CredentialsUserDTO.class);
	}

	public static CredentialsAdminDTO convertToCredentialsAdminDTO(Person person, ModelMapper modelMapper) {
		return modelMapper.map(person, CredentialsAdminDTO.class);
	}

	public static Book converToBook(BookUserDTO bookUserDTO, ModelMapper modelMapper) {
		return modelMapper.map(bookUserDTO, Book.class);
	}

	public static Book converToBook(BookAdminDTO bookAdminDTO, ModelMapper modelMapper) {
		return modelMapper.map(bookAdminDTO, Book.class);
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
