package ru.batorov.library.dto.book;

import ru.batorov.library.dto.person.PersonUserDTO;

public class BookOwnerDTO extends BookUserDTO {
	private PersonUserDTO owner;

	public PersonUserDTO getOwner() {
		return owner;
	}

	public void setOwner(PersonUserDTO personUserDTO) {
		this.owner = personUserDTO;
	}
	
}
