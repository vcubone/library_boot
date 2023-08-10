package ru.batorov.library.dto;

public class BookUsersInfoDTO extends BookUserDTO{
	public boolean expired;

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	
}
