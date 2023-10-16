package ru.batorov.library.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

import ru.batorov.library.dto.person.PersonDTOInterface;

public class RoleDTO implements PersonDTOInterface {
	@Column(name = "name")
	@NotEmpty(message = "name shouldn't be empty")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
