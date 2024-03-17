package ru.batorov.library.dto.person;

import java.util.Collection;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import ru.batorov.library.dto.book.BookUsersInfoDTO;

public class PersonWithBooksUserDTO implements PersonDTOInterface {
	private Integer id;
	
    @NotEmpty(message = "name shouldn't be empty")
    @Size(min = 2, max = 30, message = "Name between 2 and 30")
    private String fullName;
    
    @Min(value = 0, message = "yearOfBirth > 0")
    private Integer yearOfBirth;
	
	Collection<BookUsersInfoDTO> books;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(Integer yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public Collection<BookUsersInfoDTO> getBooks() {
		return books;
	}

	public void setBooks(Collection<BookUsersInfoDTO> books) {
		this.books = books;
	}
	
}
