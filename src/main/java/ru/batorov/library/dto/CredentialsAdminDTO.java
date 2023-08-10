package ru.batorov.library.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CredentialsAdminDTO {
	private Integer personId;
	
	@NotEmpty(message = "name shouldn't be empty")
    @Size(min = 2, max = 30, message = "Name between 2 and 30")
    private String username;
    
    @NotEmpty(message = "password shouldn't be empty")
    @Size(min = 2, max = 70, message = "password between 2 and 70 field")
    private String password;
	
	private String role;
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

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

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
}
