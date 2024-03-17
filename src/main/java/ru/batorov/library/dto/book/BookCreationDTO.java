package ru.batorov.library.dto.book;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class BookCreationDTO implements BookDTOInterface {
	@NotEmpty(message = "title shouldn't be empty")
	@Size(min = 2, max = 30, message = "title between 2 and 30")
	private String title;

	@NotEmpty(message = "author shouldn't be empty")
	@Size(min = 2, max = 30, message = "author between 2 and 30")
	private String author;

	@Min(value = 0, message = "release_year > 0")
	private Integer releaseYear;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}
}
