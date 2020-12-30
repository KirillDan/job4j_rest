package ru.job4j.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonControllerMockMvcTest {
	@Autowired
    private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private PersonRepository repository;
	
	@Test
	public void testPut() throws Exception {
		Integer id = 11;
		String login = UUID.randomUUID().toString();
		String password = UUID.randomUUID().toString();
		final Person savedPerson = new Person(11, login, password);
		Mockito.when(repository.save(savedPerson)).thenReturn(savedPerson);
		this.mockMvc
			.perform(put("/person/")
                    .content(objectMapper.writeValueAsString(savedPerson))
                    .contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void testDelete() throws JsonProcessingException, Exception {
		Integer id = 11;
		this.mockMvc
			.perform(delete("/person/" + id))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
