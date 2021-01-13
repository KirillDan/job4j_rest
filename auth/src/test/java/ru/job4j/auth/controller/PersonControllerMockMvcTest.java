package ru.job4j.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.job4j.auth.domain.Person;
import ru.job4j.auth.domain.Role;
import ru.job4j.auth.repository.PersonRepository;
import ru.job4j.auth.repository.RoleRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonControllerMockMvcTest {
	@Autowired
    private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private BCryptPasswordEncoder encoder;
	@MockBean
	private PersonRepository repository;
	@MockBean
	private RoleRepository roles;
	
	@BeforeEach
	private void setup() {
		String login = "admin";
		String password = "admin";
		Mockito.when(encoder.encode(password)).thenReturn(password);
		Person user = new Person(20, login, password);
		Role role = new Role();
		role.setId(1);
		role.setAuthority("ROLE_ADMIN");
		user.setRole(role);
		Mockito.when(repository.findPersonByLogin(login)).thenReturn(user);	
		Mockito.when(repository.findPersonByLogin(user.getLogin())).thenReturn(user);
	}
	
	@Test
	public void testDelete() throws JsonProcessingException, Exception {
		Integer id = 11;
		this.mockMvc
			.perform(delete("/person/" + id).header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MjQ3NDU1MjA0NX0.hBZFf9azTUv8LcdLCJSoF7XL5bmSXP_BtXidxhwLUGavqQXAxSZNpNeW6FwWt9Co5agkZdQPGngI0xgmVY9bmA"))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
