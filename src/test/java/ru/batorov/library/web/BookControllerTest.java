package ru.batorov.library.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ru.batorov.library.controllers.web.BookController;

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

	private final String WarAndPeace = "War and Peace";
	private final String TheHound = "The Hound of the Baskervilles";
	private final String EffectiveJava = "Effective Java";
	private final String adminsUsername = "ad";
	private final String userOneUsername = "user1";
	private final String userTwoUsername = "user2";
	private final String book_list = "//*[@id=\"books-list\"]/div";

	public BookControllerTest() {
	}

	@Test
	public void controllerExists() {
		assertNotNull(bookController, "no home page controller");
	}

	@Nested
	class Books {
		private final String firstBook = "//*[@id=\"books-list\"]/div[1]/a";
		private final String secondBook = "//*[@id=\"books-list\"]/div[2]/a";
		private final String thirdBook = "//*[@id=\"books-list\"]/div[3]/a";
		private final String newBook = "//*[@id=\"newBook\"]";

		@Test
		public void books() throws Exception {
			mvc.perform(get("/books")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(3))
					.andExpect(xpath(firstBook)
							.string(containsString(TheHound)))
					.andExpect(xpath(secondBook).string(containsString(WarAndPeace)))
					.andExpect(xpath(thirdBook).string(containsString(EffectiveJava)));

			mvc.perform(get("/books?sortByY")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(3))
					.andExpect(xpath(firstBook)
							.string(containsString(TheHound)))
					.andExpect(xpath(secondBook).string(containsString(WarAndPeace)))
					.andExpect(xpath(thirdBook).string(containsString(EffectiveJava)));

			mvc.perform(get("/books?sortByYear=")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(3))
					.andExpect(xpath(firstBook)
							.string(containsString(TheHound)))
					.andExpect(xpath(secondBook).string(containsString(WarAndPeace)))
					.andExpect(xpath(thirdBook).string(containsString(EffectiveJava)));

			mvc.perform(get("/books?sortByYear=false")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(3))
					.andExpect(xpath(firstBook)
							.string(containsString(TheHound)))
					.andExpect(xpath(secondBook).string(containsString(WarAndPeace)))
					.andExpect(xpath(thirdBook).string(containsString(EffectiveJava)));

			mvc.perform(get("/books?sortByYear=true")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(3))
					.andExpect(xpath(firstBook).string(containsString(WarAndPeace)))
					.andExpect(xpath(secondBook)
							.string(containsString(TheHound)))
					.andExpect(xpath(thirdBook).string(containsString(EffectiveJava)));

			mvc.perform(get("/books?sortByYear=false&page=")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(3))
					.andExpect(xpath(firstBook)
							.string(containsString(TheHound)))
					.andExpect(xpath(secondBook).string(containsString(WarAndPeace)))
					.andExpect(xpath(thirdBook).string(containsString(EffectiveJava)));

			mvc.perform(get("/books?sortByYear=false&page=1")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(3))
					.andExpect(xpath(firstBook)
							.string(containsString(TheHound)))
					.andExpect(xpath(secondBook).string(containsString(WarAndPeace)))
					.andExpect(xpath(thirdBook).string(containsString(EffectiveJava)));

			mvc.perform(get("/books?sortByYear=false&page=1&")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(3))
					.andExpect(xpath(firstBook)
							.string(containsString(TheHound)))
					.andExpect(xpath(secondBook).string(containsString(WarAndPeace)))
					.andExpect(xpath(thirdBook).string(containsString(EffectiveJava)));

			mvc.perform(get("/books?sortByYear=false&page=1&itemsP")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(3))
					.andExpect(xpath(firstBook)
							.string(containsString(TheHound)))
					.andExpect(xpath(secondBook).string(containsString(WarAndPeace)))
					.andExpect(xpath(thirdBook).string(containsString(EffectiveJava)));

			mvc.perform(get("/books?sortByYear=false&page=1&itemsPerPage=")).andDo(print())
					.andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(3))
					.andExpect(xpath(firstBook)
							.string(containsString(TheHound)))
					.andExpect(xpath(secondBook).string(containsString(WarAndPeace)))
					.andExpect(xpath(thirdBook).string(containsString(EffectiveJava)));

			mvc.perform(get("/books?sortByYear=true&page=-1&itemsPerPage=2")).andDo(print())
					.andExpect(status().isBadRequest());
			mvc.perform(get("/books?sortByYear=true&page=1&itemsPerPage=-2")).andDo(print())
					.andExpect(status().isBadRequest());

			mvc.perform(get("/books?sortByYear=true&page=0&itemsPerPage=2")).andDo(print())
					.andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(2))
					.andExpect(xpath(firstBook).string(containsString(WarAndPeace)))
					.andExpect(xpath(secondBook)
							.string(containsString(TheHound)));

			mvc.perform(get("/books?sortByYear=false&page=0&itemsPerPage=2")).andDo(print())
					.andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(2))
					.andExpect(xpath(firstBook)
							.string(containsString(TheHound)))
					.andExpect(xpath(secondBook).string(containsString(WarAndPeace)));

			mvc.perform(get("/books?sortByYear=true&page=1&itemsPerPage=2")).andDo(print())
					.andExpect(status().isOk())
					.andExpect(xpath(book_list).nodeCount(1))
					.andExpect(xpath(firstBook).string(containsString(EffectiveJava)));
		}

		@Test
		public void newBookDoesNotExistsForUnAuthorized() throws Exception {
			mvc.perform(get("/books")).andDo(print())
					.andExpect(status().isOk())
					.andExpect(xpath(newBook).doesNotExist());
		}

		@WithUserDetails(userOneUsername)
		@Test
		public void newBookDoesNotExistsForUser() throws Exception {
			mvc.perform(get("/books")).andDo(print())
					.andExpect(status().isOk())
					.andExpect(xpath(newBook).doesNotExist());
		}

		@WithUserDetails(adminsUsername)
		@Test
		public void newBookExistsForAdmin() throws Exception {
			mvc.perform(get("/books")).andDo(print())
					.andExpect(status().isOk())
					.andExpect(xpath(newBook).exists());
		}
	}

	@Nested
	class BooksSearch {
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
			mvc.perform(get("/books/search")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(search).exists())
					.andExpect(xpath(findResult).doesNotExist());

			mvc.perform(get("/books/search?findReq")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(search).exists())
					.andExpect(xpath(findResult).doesNotExist());

			mvc.perform(get("/books/search?findRequest=")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(search).exists())
					.andExpect(xpath(findResult).doesNotExist());

			mvc.perform(get("/books/search?findRequest=Abra+Kadabra")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(search).exists())
					.andExpect(xpath(findResult).exists())
					.andExpect(xpath(noBooks).exists())
					.andExpect(xpath(books).doesNotExist());

			mvc.perform(get("/books/search?findRequest=Effective")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(search).exists())
					.andExpect(xpath(findResult).exists())
					.andExpect(xpath(noBooks).doesNotExist())
					.andExpect(xpath(books).exists())
					.andExpect(xpath(onlyOneBook).string(containsString(EffectiveJava)));

			mvc.perform(get("/books/search?findRequest=a")).andDo(print()).andExpect(status().isOk())
					.andExpect(xpath(search).exists())
					.andExpect(xpath(findResult).exists())
					.andExpect(xpath(noBooks).doesNotExist())
					.andExpect(xpath(books).exists())
					.andExpect(xpath(firstBook).string(containsString("The Hound of")))
					.andExpect(xpath(secondBook).string(containsString("War and")))
					.andExpect(xpath(thirdBook).string(containsString("Effective")));
		}
	}

	@Nested
	class BookId {
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
		@WithUserDetails(userOneUsername)
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
		@WithUserDetails(userTwoUsername)
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
		@WithUserDetails(adminsUsername)
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

	@Nested
	class NewBook {
		@Test
		public void accessDeniedUnAuthorized() throws Exception {
			mvc.perform(get("/books/new")).andDo(print()).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("http://localhost/auth/login"));
		}

		@Test
		@WithUserDetails(userOneUsername)
		public void accessDeniedUser() throws Exception {
			mvc.perform(get("/books/new")).andDo(print()).andExpect(status().isForbidden());
		}

		@Test
		@WithUserDetails(adminsUsername)
		public void getRequestAdmin() throws Exception {
			mvc.perform(get("/books/new")).andDo(print()).andExpect(status().isOk());
		}

		@Test
		@WithUserDetails(adminsUsername)
		public void createNewBookAdmin() throws Exception {
			mvc.perform(post("/books/new").contentType(MediaType.APPLICATION_FORM_URLENCODED).content("title=newTitle&author=newAuthor&releaseYear=1").with(SecurityMockMvcRequestPostProcessors.csrf()))
					.andDo(print()).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/books"));

			mvc.perform(get("/books/4")).andDo(print()).andExpect(status().isOk());//TODO проверить, что это нужная книга
		}

	}

}
