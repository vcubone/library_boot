package ru.batorov.library.rest;

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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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

	@Test
	public void books() throws Exception {
		this.mvc.perform(get("/api/books").contentType("APPLICATION_JSON").characterEncoding("UTF-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				;
	}
	
	@Test
	public void accesDeniedAccount() throws Exception {
		this.mvc.perform(get("/api/account/main")).andDo(print()).andExpect(status().isForbidden());
		this.mvc.perform(patch("/account/edit")).andDo(print()).andExpect(status().isForbidden());
		this.mvc.perform(patch("/credentials/edit")).andDo(print()).andExpect(status().isForbidden());
	}
}
