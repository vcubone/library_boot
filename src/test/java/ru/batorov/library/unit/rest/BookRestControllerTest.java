package ru.batorov.library.unit.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.mysql.cj.exceptions.AssertionFailedException;

import ru.batorov.library.controllers.rest.BookRestController;
import ru.batorov.library.dto.book.BookUserDTO;
import ru.batorov.library.models.Book;
import ru.batorov.library.models.Person;
import ru.batorov.library.repositories.BookRepository;
import ru.batorov.library.repositories.PeopleRepository;
import ru.batorov.library.repositories.RolesRepository;
import ru.batorov.library.services.BookService;
import ru.batorov.library.util.DTOConvert;
import ru.batorov.library.util.exceptions.BookNotFoundException;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static ru.batorov.library.util.DTOConvert.*;

/**
 * Book controller unit test class
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/unit/application-test.properties")
public class BookRestControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private BookRestController bookRestController;
	@Autowired
	private ModelMapper modelMapper;
	@MockBean
	private BookService bookService;
	@MockBean
	private BookRepository bookRepository;
	@MockBean
	private PeopleRepository peopleRepository;
	@MockBean
	private RolesRepository rolesRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private final String URL = "/api/books";

	public BookRestControllerTest() {
	}

	/**
	 * Test existence of controller class
	 */
	@Test
	public void controllerExists() {
		assertNotNull(bookRestController, "no BookRestController");
	}

	/**
	 * Test /books get request
	 * 
	 * @throws Exception
	 */
	@Test
	public void allTest() throws Exception {
		CollectionType constructCollectionType = objectMapper.getTypeFactory()
				.constructCollectionType(Collection.class, BookUserDTO.class);
		Book firstBook = new Book("title1", "author1", 10);
		firstBook.setId(1);
		Book secondBook = new Book("title2", "author2", 1);
		secondBook.setId(2);
		Book thirdBook = new Book("title3", "author3", 20);
		thirdBook.setId(3);

		List<Book> books = List.of(firstBook, secondBook, thirdBook);
		Collection<BookUserDTO> expected = DTOConvert.convertToBookUserDTOCollection(books, modelMapper);
		Mockito.when(bookService.all(false)).thenReturn(books);

		MvcResult result = mvc.perform(get(URL)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		String content = result.getResponse().getContentAsString();

		Collection<BookUserDTO> actual = objectMapper.readValue(content, constructCollectionType);
		assertTrue(expected.equals(actual));

		books = List.of(secondBook, firstBook, thirdBook);
		expected = DTOConvert.convertToBookUserDTOCollection(books, modelMapper);
		Mockito.when(bookService.all(true)).thenReturn(books);
		result = mvc.perform(get(URL).param("sortByYear", "true")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		content = result.getResponse().getContentAsString();
		actual = objectMapper.readValue(content, constructCollectionType);
		assertTrue(expected.equals(actual));

		books = List.of(secondBook, firstBook);
		expected = DTOConvert.convertToBookUserDTOCollection(books, modelMapper);
		Mockito.when(bookService.all(Mockito.eq(true), Mockito.any(), Mockito.any())).thenReturn(books);
		result = mvc.perform(get(URL).param("sortByYear", "true").param("page", "1").param("itemsPerPage", "2"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		content = result.getResponse().getContentAsString();
		actual = objectMapper.readValue(content, constructCollectionType);
		assertTrue(expected.equals(actual));
	}

	/**
	 * Test of /search request
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchTest() throws Exception {
		String SEARCH_URL = URL + "/search";
		CollectionType constructCollectionType = objectMapper.getTypeFactory()
				.constructCollectionType(Collection.class, BookUserDTO.class);
		Book firstBook = new Book("title1", "author1", 10);
		firstBook.setId(1);
		Book secondBook = new Book("tit le2", "author2", 1);
		secondBook.setId(2);
		Book thirdBook = new Book("newtitle3", "author3", 20);
		thirdBook.setId(3);

		List<Book> books = List.of(firstBook, secondBook, thirdBook);
		searchRequestAndAssert("t", SEARCH_URL, books, constructCollectionType);

		books = List.of(firstBook, secondBook);
		searchRequestAndAssert("tit", SEARCH_URL, books, constructCollectionType);

		books = List.of(firstBook);
		searchRequestAndAssert("title", SEARCH_URL, books, constructCollectionType);
		
		books = Collections.EMPTY_LIST;
		searchRequestAndAssert("abc", SEARCH_URL, books, constructCollectionType);
	}

	private void searchRequestAndAssert(String searchRequest, String URL, List<Book> books,
			CollectionType constructCollectionType) throws Exception {
		Collection<BookUserDTO> expected = DTOConvert.convertToBookUserDTOCollection(books, modelMapper);
		Mockito.when(bookService.findBooksByTitleContaining(Mockito.any())).thenReturn(books);
		MvcResult result = mvc.perform(get(URL).param("findRequest", "searchRequest")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		Collection<BookUserDTO> actual = objectMapper.readValue(content, constructCollectionType);
		assertTrue(expected.equals(actual));
		assertEquals(expected, actual);
	}

	/**
	 * Test of /id with unauthorized user
	 * @throws Exception
	 */
	@Test
	public void idUnauthorizedTest() throws Exception {
		String ID_URL = URL + "/";
		Book firstBook = new Book("title1", "author1", 10);
		firstBook.setId(1);
		Person personOne = new Person();
		personOne.setId(10);
		personOne.setFullName("personOne");
		personOne.setYearOfBirth(2000);
		firstBook.setOwner(personOne);
		Book secondBook = new Book("title2", "author2", 20);
		secondBook.setId(3);
		secondBook.setOwner(personOne);
		Book thirdBook = new Book("title3", "author3", 30);
		thirdBook.setId(5);

		Mockito.when(bookService.getBookByIdWithOwner(1)).thenReturn(Book.shallowCopy(firstBook));
		MvcResult result = mvc.perform(get(ID_URL + 1)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		Book expectedCopy = Book.shallowCopy(firstBook);
		expectedCopy.setOwner(new Person());
		BookUserDTO expectedObj = convertToBookOwnerDTO(expectedCopy, modelMapper);
		String expectedStr = objectMapper.writeValueAsString(expectedObj);
		assertEquals(expectedStr, content);
		
		Mockito.when(bookService.getBookByIdWithOwner(3)).thenReturn(Book.shallowCopy(secondBook));
		result = mvc.perform(get(ID_URL + 3)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		content = result.getResponse().getContentAsString();
		expectedCopy = Book.shallowCopy(secondBook);
		expectedCopy.setOwner(new Person());
		expectedObj = convertToBookOwnerDTO(expectedCopy, modelMapper);
		expectedStr = objectMapper.writeValueAsString(expectedObj);
		assertEquals(expectedStr, content);
		
		Mockito.when(bookService.getBookByIdWithOwner(5)).thenReturn(thirdBook);
		result = mvc.perform(get(ID_URL + 5)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		content = result.getResponse().getContentAsString();
		expectedObj = convertToBookOwnerDTO(thirdBook, modelMapper);
		expectedStr = objectMapper.writeValueAsString(expectedObj);
		assertEquals(expectedStr, content);
		
		Mockito.when(bookService.getBookByIdWithOwner(100)).thenThrow(new BookNotFoundException());
		mvc.perform(get(ID_URL + 100)).andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string(containsString("There is no book with the required id")));
	}

}