package ru.batorov.library.dto;

public class BookOwnerDTO extends BookUserDTO {
	private PersonUserDTO personUserDTO;

	public PersonUserDTO getPersonUserDTO() {
		return personUserDTO;
	}

	public void setPersonUserDTO(PersonUserDTO personUserDTO) {
		this.personUserDTO = personUserDTO;
	}
	
}
