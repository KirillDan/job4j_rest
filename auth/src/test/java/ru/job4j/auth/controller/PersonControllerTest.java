package ru.job4j.auth.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {
	private static final String ROOT_URL = "http://localhost:";
	
	@Autowired
    private TestRestTemplate template;
	@LocalServerPort
	private int port;
	@MockBean
	private PersonRepository repository;
	
	private List<Person> person;
	
	@BeforeEach
	private void setup() {
		this.person = new ArrayList();
		for (int i = 0; i < 10; i++) {
			this.person.add(new Person(i, UUID.randomUUID().toString(), UUID.randomUUID().toString()));
			Mockito.when(repository.findById(i)).thenReturn(Optional.of(this.person.get(i)));
		}
		Mockito.when(repository.findAll()).thenReturn(this.person);
	}
	
	@Test
	public void testFindAll() {
		ResponseEntity<Person[]> responseEntity = template.getForEntity("/person/", Person[].class);
		assertTrue(this.person.containsAll(Arrays.asList(responseEntity.getBody())));
	}
	
	@Test
	public void testFindById() {
		for (Person person2 : person) {
			int id = person2.getId();
			assertEquals(this.person.get(id), template.getForEntity("/person/" + id, Person.class).getBody());
		}
	}
	
	@Test
	public void testPost() {
		String login = UUID.randomUUID().toString();
		String password = UUID.randomUUID().toString();
		Person savedPerson = new Person(11, login, password);
		Mockito.when(repository.save(savedPerson)).thenReturn(savedPerson);
		Person findedPerson = template.postForEntity("/person/", savedPerson, Person.class).getBody();
		assertEquals(findedPerson.getLogin(), savedPerson.getLogin());
		assertEquals(findedPerson.getPassword(), savedPerson.getPassword());
	}
}
