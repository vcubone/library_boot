package ru.batorov.library.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertNotNull;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ru.batorov.library.controllers.web.BookController;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = { "/create-books-before.sql",
		"/create-users-before.sql",
		"/people-take-books-before.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = { "/create-books-after.sql",
		"/create-users-after.sql", "/people-take-books-after.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class BookControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private BookController bookController;

	public BookControllerTest() {
	}

	@Test
	public void controllerExists() {
		assertNotNull("no home page controller", bookController);
	}

	@Test
	public void books() throws Exception {
		this.mvc.perform(get("/books")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a")
						.string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")));

		this.mvc.perform(get("/books?sortByY")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a")
						.string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")));

		this.mvc.perform(get("/books?sortByYear=")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a")
						.string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")));

		this.mvc.perform(get("/books?sortByYear=false")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a")
						.string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")));

		this.mvc.perform(get("/books?sortByYear=true")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a")
						.string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")));

		this.mvc.perform(get("/books?sortByYear=false&page=")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a")
						.string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")));

		this.mvc.perform(get("/books?sortByYear=false&page=1")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a")
						.string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")));

		this.mvc.perform(get("/books?sortByYear=false&page=1&")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a")
						.string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")));

		this.mvc.perform(get("/books?sortByYear=false&page=1&itemsP")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a")
						.string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")));

		this.mvc.perform(get("/books?sortByYear=false&page=1&itemsPerPage=")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a")
						.string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")));

		this.mvc.perform(get("/books?sortByYear=true&page=-1&itemsPerPage=2")).andDo(print())
				.andExpect(status().isBadRequest());
		this.mvc.perform(get("/books?sortByYear=true&page=1&itemsPerPage=-2")).andDo(print())
				.andExpect(status().isBadRequest());

		this.mvc.perform(get("/books?sortByYear=true&page=0&itemsPerPage=2")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"))
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(2))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a")
						.string(containsString("The Hound of the Baskervilles")));

		this.mvc.perform(get("/books?sortByYear=false&page=0&itemsPerPage=2")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"))
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(2))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a")
						.string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")));

		this.mvc.perform(get("/books?sortByYear=true&page=1&itemsPerPage=2")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"))
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(1))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("Effective Java")));
	}

	private final String search = "//*[@id=\"search\"]";
	private final String findResult = "//*[@id=\"findResult\"]";
	private final String noBooks = "//*[@id=\"noBooks\"]";
	private final String books = "//*[@id=\"books\"]";
	private final String onlyOneBook = "//*[@id=\"books\"]/div";
	private final String firstBook = "//*[@id=\"books\"]/div[1]";
	private final String secondBook = "//*[@id=\"books\"]/div[2]";
	private final String thirdBook = "//*[@id=\"books\"]/div[3]";
	@Test
	public void booksSearch() throws Exception {
		this.mvc.perform(get("/books/search")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(search).exists())
				.andExpect(xpath(findResult).doesNotExist());

		this.mvc.perform(get("/books/search?findReq")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(search).exists())
				.andExpect(xpath(findResult).doesNotExist());

		this.mvc.perform(get("/books/search?findRequest=")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(search).exists())
				.andExpect(xpath(findResult).doesNotExist());

		this.mvc.perform(get("/books/search?findRequest=Abra+Kadabra")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(search).exists())
				.andExpect(xpath(findResult).exists())
				.andExpect(xpath(noBooks).exists())
				.andExpect(xpath(books).doesNotExist());

		this.mvc.perform(get("/books/search?findRequest=Effective")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(search).exists())
				.andExpect(xpath(findResult).exists())
				.andExpect(xpath(noBooks).doesNotExist())
				.andExpect(xpath(books).exists())
				.andExpect(xpath(onlyOneBook).string(containsString("Effective Java")));
				
		this.mvc.perform(get("/books/search?findRequest=a")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(search).exists())
				.andExpect(xpath(findResult).exists())
				.andExpect(xpath(noBooks).doesNotExist())
				.andExpect(xpath(books).exists())
				.andExpect(xpath(firstBook).string(containsString("The Hound of")))
				.andExpect(xpath(secondBook).string(containsString("War and")))
				.andExpect(xpath(thirdBook).string(containsString("Effective")));
	}

	
	
	
	private final String addownerAdmin = "//*[@id=\"addownerAdmin\"]";
	private final String releaseAdmin = "//*[@id=\"releaseAdmin\"]";
	private final String editAdmin = "//*[@id=\"editAdmin\"]";
	private final String addownerUser = "//*[@id=\"addownerUser\"]/input[3]";
	private final String releaseUser = "//*[@id=\"releaseUser\"]/input[3]";
	private final String owner = "//*[@id=\"Owner\"]";
	private final String noOwner = "//*[@id=\"noOwner\"]";

	@Test
	public void bookIdUnAuthorized() throws Exception {
		mvc.perform(get("/books/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).exists())
				.andExpect(xpath(noOwner).doesNotExist())
				.andExpect(xpath(releaseAdmin).doesNotExist())
				.andExpect(xpath(editAdmin).doesNotExist())
				.andExpect(xpath(addownerUser).doesNotExist());

		mvc.perform(get("/books/2")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).exists())
				.andExpect(xpath(noOwner).doesNotExist())
				.andExpect(xpath(releaseAdmin).doesNotExist())
				.andExpect(xpath(editAdmin).doesNotExist())
				.andExpect(xpath(addownerUser).doesNotExist());

		mvc.perform(get("/books/3")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).doesNotExist())
				.andExpect(xpath(noOwner).exists())
				.andExpect(xpath(addownerAdmin).doesNotExist())
				.andExpect(xpath(editAdmin).doesNotExist())
				.andExpect(xpath(releaseUser).doesNotExist());

		mvc.perform(get("/books/100")).andDo(print()).andExpect(status().isNotFound());

		mvc.perform(get("/books/-100")).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	@WithUserDetails("user1")
	public void bookIdUserWithBooks() throws Exception {
		mvc.perform(get("/books/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).exists())
				.andExpect(xpath(noOwner).doesNotExist())
				.andExpect(xpath(releaseAdmin).doesNotExist())
				.andExpect(xpath(editAdmin).doesNotExist())
				.andExpect(xpath(releaseUser).exists());

		mvc.perform(get("/books/2")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).exists())
				.andExpect(xpath(noOwner).doesNotExist())
				.andExpect(xpath(releaseAdmin).doesNotExist())
				.andExpect(xpath(editAdmin).doesNotExist())
				.andExpect(xpath(releaseUser).exists());

		mvc.perform(get("/books/3")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).doesNotExist())
				.andExpect(xpath(noOwner).exists())
				.andExpect(xpath(addownerAdmin).doesNotExist())
				.andExpect(xpath(editAdmin).doesNotExist())
				.andExpect(xpath(addownerUser).exists());
	}

	@Test
	@WithUserDetails("user2")
	public void bookIdUserNoBooks() throws Exception {
		mvc.perform(get("/books/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).exists())
				.andExpect(xpath(noOwner).doesNotExist())
				.andExpect(xpath(releaseAdmin).doesNotExist())
				.andExpect(xpath(editAdmin).doesNotExist())
				.andExpect(xpath(releaseUser).doesNotExist());

		mvc.perform(get("/books/2")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).exists())
				.andExpect(xpath(noOwner).doesNotExist())
				.andExpect(xpath(releaseAdmin).doesNotExist())
				.andExpect(xpath(editAdmin).doesNotExist())
				.andExpect(xpath(releaseUser).doesNotExist());

		mvc.perform(get("/books/3")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).doesNotExist())
				.andExpect(xpath(noOwner).exists())
				.andExpect(xpath(addownerAdmin).doesNotExist())
				.andExpect(xpath(editAdmin).doesNotExist())
				.andExpect(xpath(addownerUser).exists());
	}

	@Test
	@WithUserDetails("ad")
	public void bookIdAdmin() throws Exception {
		mvc.perform(get("/books/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).exists())
				.andExpect(xpath(noOwner).doesNotExist())
				.andExpect(xpath(releaseAdmin).exists())
				.andExpect(xpath(editAdmin).exists())
				.andExpect(xpath(releaseUser).doesNotExist());

		mvc.perform(get("/books/2")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).exists())
				.andExpect(xpath(noOwner).doesNotExist())
				.andExpect(xpath(releaseAdmin).exists())
				.andExpect(xpath(editAdmin).exists())
				.andExpect(xpath(releaseUser).doesNotExist());

		mvc.perform(get("/books/3")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath(owner).doesNotExist())
				.andExpect(xpath(noOwner).exists())
				.andExpect(xpath(addownerAdmin).exists())
				.andExpect(xpath(editAdmin).exists())
				.andExpect(xpath(addownerUser).doesNotExist());
	}

}
