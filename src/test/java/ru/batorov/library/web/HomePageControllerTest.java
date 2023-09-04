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

import ru.batorov.library.controllers.web.HomePageController;

import static org.junit.Assert.assertNotNull;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = { "/create-books-before.sql", "/create-users-before.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = { "/create-books-after.sql", "/create-users-after.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class HomePageControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private HomePageController homePageController;

	public HomePageControllerTest() {
	}

	@Test
	public void controllerExists() {
		assertNotNull("no home page controller", homePageController);
	}

	@Test
	public void unAuthorized() throws Exception {
		this.mvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header[1]/div/div/header/ul/li[3]/a").string("Войти в аккаунт"));
	}

	@Test
	@WithUserDetails("user1")
	public void userAuthorized() throws Exception {
		this.mvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header/div/div/header/ul/li[3]/a").string("Мой профиль"));
	}

	@Test
	@WithUserDetails("ad")
	public void adminAuthorized() throws Exception {
		this.mvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(xpath("/html/body/header/div/div/header/ul/li[3]/a").string("Читатели"))
				.andExpect(xpath("/html/body/header/div/div/header/ul/li[4]/a").string("Админка"))
				.andExpect(xpath("/html/body/header/div/div/header/ul/li[5]/a").string("Мой профиль"));
	}
}
