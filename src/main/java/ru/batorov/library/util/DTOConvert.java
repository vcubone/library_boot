package ru.batorov.library.util;

import java.util.List;

import org.modelmapper.ModelMapper;

import ru.batorov.library.dto.BookAdminDTO;
import ru.batorov.library.dto.BookUserDTO;
import ru.batorov.library.dto.BookUsersInfoDTO;
import ru.batorov.library.dto.CredentialsAdminDTO;
import ru.batorov.library.dto.CredentialsUserDTO;
import ru.batorov.library.dto.PersonAdminDTO;
import ru.batorov.library.dto.PersonUserDTO;
import ru.batorov.library.models.Book;
import ru.batorov.library.models.Credentials;
import ru.batorov.library.models.Person;

public class DTOConvert {
	private DTOConvert(){throw new AssertionError();}
	
	public static Person converToPerson(PersonUserDTO personDTO, ModelMapper modelMapper) {
		return modelMapper.map(personDTO, Person.class);
	}
	public static Person converToPerson(PersonAdminDTO personAdminDTO, ModelMapper modelMapper) {
		return modelMapper.map(personAdminDTO, Person.class);
	}
	public static PersonUserDTO convertToPersonUserDTO(Person person, ModelMapper modelMapper){
		return modelMapper.map(person, PersonUserDTO.class);
	}
	public static PersonAdminDTO convertToPersonAdminDTO(Person person, ModelMapper modelMapper){
		return modelMapper.map(person, PersonAdminDTO.class);
	}
	public static List<PersonUserDTO> convertToPersonUserDTOList(List<Person> persons, ModelMapper modelMapper){
		return persons.stream().map(person -> DTOConvert.convertToPersonUserDTO(person, modelMapper)).toList();
	}
	public static List<PersonAdminDTO> convertToPersonAdminDTOList(List<Person> persons, ModelMapper modelMapper){
		return persons.stream().map(person -> DTOConvert.convertToPersonAdminDTO(person, modelMapper)).toList();
	}
	
	public static Credentials converToCredentials(CredentialsUserDTO CredentialsUserDTO, ModelMapper modelMapper) {
		return modelMapper.map(CredentialsUserDTO, Credentials.class);
	}
	public static Credentials converToCredentials(CredentialsAdminDTO credentialsAdminDTO, ModelMapper modelMapper) {
		return modelMapper.map(credentialsAdminDTO, Credentials.class);
	}
	public static CredentialsUserDTO convertToCredentialsUserDTO(Credentials credentials, ModelMapper modelMapper){
		return modelMapper.map(credentials, CredentialsUserDTO.class);
	}
	public static CredentialsAdminDTO convertToCredentialsAdminDTO(Credentials credentials, ModelMapper modelMapper){
		return modelMapper.map(credentials, CredentialsAdminDTO.class);
	}
	
	public static Book converToBook(BookUserDTO bookUserDTO, ModelMapper modelMapper) {
		return modelMapper.map(bookUserDTO, Book.class);
	}
	public static Book converToBook(BookAdminDTO bookAdminDTO, ModelMapper modelMapper) {
		return modelMapper.map(bookAdminDTO, Book.class);
	}
	public static BookUserDTO convertToBookUserDTO(Book book, ModelMapper modelMapper){
		return modelMapper.map(book, BookUserDTO.class);
	}
	public static List<BookUserDTO> convertToBookUserDTOList(List<Book> books, ModelMapper modelMapper){
		return books.stream().map(book -> DTOConvert.convertToBookUserDTO(book, modelMapper)).toList();
	}
	public static List<BookAdminDTO> convertToBookAdminDTOList(List<Book> books, ModelMapper modelMapper){
		return books.stream().map(book -> DTOConvert.convertToBookAdminDTO(book, modelMapper)).toList();
	}
	public static BookUsersInfoDTO convertToBookUsersInfoDTO(Book book, ModelMapper modelMapper){
		return modelMapper.map(book, BookUsersInfoDTO.class);
	}
	public static BookAdminDTO convertToBookAdminDTO(Book book, ModelMapper modelMapper){
		return modelMapper.map(book, BookAdminDTO.class);
	}

}
