package ru.batorov.library.dto.credentials;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import ru.batorov.library.dto.person.PersonDTOInterface;

public class PasswordDTO implements PersonDTOInterface {
	@NotEmpty(message = "password shouldn't be empty")
    @Size(min = 2, max = 10, message = "password between 2 and 10 field")
    private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
