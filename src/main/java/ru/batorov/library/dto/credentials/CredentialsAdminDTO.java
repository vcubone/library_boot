package ru.batorov.library.dto.credentials;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import ru.batorov.library.dto.person.PersonDTOInterface;

public class CredentialsAdminDTO implements PersonDTOInterface {
	private Integer id;
	
	@NotEmpty(message = "name shouldn't be empty")
    @Size(min = 2, max = 30, message = "Name between 2 and 30")
    private String username;
    
    @NotEmpty(message = "password shouldn't be empty")
    @Size(min = 2, max = 10, message = "password between 2 and 10 field")
    private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
