package ru.batorov.library.util.exceptions;

public class BookNotFoundException extends RuntimeException {
	public BookNotFoundException()
	{
		this("There is no book with the required id");
	}
	public BookNotFoundException(String message)
	{
		super(message);
	}
	public BookNotFoundException(Integer bookId)
	{
		super("There is no book with id = " + bookId);
	}
}
