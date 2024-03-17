package ru.batorov.library.dto.person;

import java.util.Collection;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import ru.batorov.library.dto.RoleDTO;
import ru.batorov.library.dto.book.BookAdminDTO;

public class PersonWithBooksAdminDTO implements PersonDTOInterface {
	private Integer id;
	
    @NotEmpty(message = "name shouldn't be empty")
    @Size(min = 2, max = 30, message = "Name between 2 and 30")
    private String fullName;
    
    @Min(value = 0, message = "yearOfBirth > 0")
    private Integer yearOfBirth;

	Collection<BookAdminDTO> books;
	
	Collection<RoleDTO> roles;

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

	public Collection<BookAdminDTO> getBooks() {
		return books;
	}

	public void setBooks(Collection<BookAdminDTO> books) {
		this.books = books;
	}

	public Collection<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RoleDTO> roles) {
		this.roles = roles;
	}
	
	
}
