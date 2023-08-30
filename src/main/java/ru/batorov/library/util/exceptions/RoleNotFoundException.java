package ru.batorov.library.util.exceptions;

public class RoleNotFoundException extends RuntimeException {
	public RoleNotFoundException()
	{
		this("There is no role with the required name");
	}

	public RoleNotFoundException(String name)
	{
		super("There is no person with name = " + name);
	}
}
