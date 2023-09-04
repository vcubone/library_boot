package ru.batorov.library.web;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ru.batorov.library.controllers.web.HomePageController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = { "/create-books-before.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = { "/create-books-after.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class UnAuthorizedUserTests {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private HomePageController homePageController;

	public UnAuthorizedUserTests() {
	}

	@Test
	public void controllerExists() {
		assertNotNull("no home page controller", homePageController);
	}

	@Test
	public void homePage() throws Exception {
		this.mvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"));
	}

	@Test
	public void books() throws Exception {
		this.mvc.perform(get("/books")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"))
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")));

		this.mvc.perform(get("/books?sortByY")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")))
				;
				
		this.mvc.perform(get("/books?sortByYear=")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")))
				;
				
		this.mvc.perform(get("/books?sortByYear=false")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")))
				;
				
		this.mvc.perform(get("/books?sortByYear=true")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"))
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")))
				;
				
		this.mvc.perform(get("/books?sortByYear=true&page=1")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"))
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")))
				;
				
		this.mvc.perform(get("/books?sortByYear=true&page=")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"))
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(3))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[3]/a").string(containsString("Effective Java")))
				;

		this.mvc.perform(get("/books?sortByYear=true&page=0&itemsPerPage=2")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"))
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(2))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("War and Peace")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("The Hound of the Baskervilles")))
				;
				
		this.mvc.perform(get("/books?sortByYear=false&page=0&itemsPerPage=2")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"))
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(2))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("The Hound of the Baskervilles")))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[2]/a").string(containsString("War and Peace")))
				;
		
		this.mvc.perform(get("/books?sortByYear=true&page=1&itemsPerPage=2")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"))
				.andExpect(xpath("//*[@id=\"books-list\"]/div").nodeCount(1))
				.andExpect(xpath("//*[@id=\"books-list\"]/div[1]/a").string(containsString("Effective Java")))
				;
				
		
	}

	@Test
	public void booksSearch() throws Exception {
		this.mvc.perform(get("/books/search")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"));
	}

	@Test
	public void accesDeniedAccount() throws Exception {
		this.mvc.perform(get("/account/main")).andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/auth/login"));
		this.mvc.perform(get("/account/edit")).andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/auth/login"));
		this.mvc.perform(patch("/account/edit")).andDo(print()).andExpect(status().is4xxClientError());
		this.mvc.perform(get("/credentials/edit")).andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/auth/login"));
		this.mvc.perform(patch("/credentials/edit")).andDo(print()).andExpect(status().is4xxClientError());
	}

	@Test
	public void accesDeniedPeople() throws Exception {
		this.mvc.perform(get("/people")).andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/auth/login"));
		this.mvc.perform(get("/people/new")).andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/auth/login"));
		for (int i = -1; i < 5; i++) {
			this.mvc.perform(get("/people/" + i)).andDo(print()).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("http://localhost/auth/login"));

			this.mvc.perform(get("/people/" + i + "/edit")).andDo(print()).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("http://localhost/auth/login"));
			this.mvc.perform(patch("/people/" + i + "/edit")).andDo(print()).andExpect(status().is4xxClientError());

			this.mvc.perform(get("/people/credentials/" + i + "/edit")).andDo(print())
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("http://localhost/auth/login"));
			this.mvc.perform(patch("/people/credentials/" + i + "/edit")).andDo(print())
					.andExpect(status().is4xxClientError());

			this.mvc.perform(delete("/people/" + i)).andDo(print()).andExpect(status().is4xxClientError());
			
			this.mvc.perform(patch("/people/" + i + "/edit/deleterole")).andDo(print()).andExpect(status().is4xxClientError());
			this.mvc.perform(patch("/people/" + i + "/edit/addrole")).andDo(print()).andExpect(status().is4xxClientError());
		}
	}
	// TODO доделать acces denied redirect login

	@Test
	public void login() throws Exception {
		this.mvc.perform(get("/auth/login")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Введите имя пользователя")));
	}

	@Test
	public void register() throws Exception {
		this.mvc.perform(get("/auth/register")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Введите username")));
	}

	// TODO post register check db

	@Test
	public void badCredentials() throws Exception {
		this.mvc.perform(post("/process_login").param("user", "test")).andDo(print()).andExpect(status().isForbidden());
	}

}