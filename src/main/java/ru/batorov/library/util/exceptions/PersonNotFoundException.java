package ru.batorov.library.util.exceptions;


public class PersonNotFoundException extends RuntimeException {
	public PersonNotFoundException()
	{
		this("There is no person with the required id");
	}
	public PersonNotFoundException(String message)
	{
		super(message);
	}
	public PersonNotFoundException(Integer personId)
	{
		super("There is no person with id = " + personId);
	}
}
