package ru.job4j.auth.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.domain.Role;
import ru.job4j.auth.repository.PersonRepository;
import ru.job4j.auth.repository.RoleRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {
	@Autowired
    private TestRestTemplate template;
	@MockBean
	private BCryptPasswordEncoder encoder;
	@LocalServerPort
	private int port;
	@MockBean
	private PersonRepository repository;
	@MockBean
	private RoleRepository roles;
	
	private List<Person> person;
	
	@BeforeEach
	private void setup() {
		this.person = new ArrayList();
		for (int i = 0; i < 10; i++) {
			this.person.add(new Person(i, UUID.randomUUID().toString(), UUID.randomUUID().toString()));
			Mockito.when(repository.findById(i)).thenReturn(Optional.of(this.person.get(i)));
		}
		Mockito.when(repository.findAll()).thenReturn(this.person);
		
		String login = "ivan";
		String password = "ivan";
		Mockito.when(encoder.encode(password)).thenReturn(password);
		Person user = new Person(20, login, password);
		Role role = new Role();
		role.setId(1);
		role.setAuthority("ROLE_USER");
		user.setRole(role);
		Mockito.when(repository.findPersonByLogin(login)).thenReturn(user);	
		Mockito.when(repository.findPersonByLogin(user.getLogin())).thenReturn(user);
	}
	
	@Test
	public void testFindAll() throws URISyntaxException {
		MultiValueMap<String,String> headers = new HttpHeaders();
		headers.add("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuIiwiZXhwIjoyNDc0NTUxMTI1fQ.r6md4OvfY2Crs9IzAu8d8rhInKhHMMig7xn1YUKFO6I4QwmIO1Q5mo5zQXPdaTdvbVZe8JUW2wp784fXEuW8SQ");
		RequestEntity<Void> requestEntity = new RequestEntity<Void>(headers, HttpMethod.GET, new URI("http://localhost:" + this.port + "/person/"));
		ResponseEntity<Person[]> responseEntity = template.exchange(requestEntity, Person[].class);
		assertTrue(this.person.containsAll(Arrays.asList(responseEntity.getBody())));
	}
	
	@Test
	public void testFindById() throws URISyntaxException {
		MultiValueMap<String,String> headers = new HttpHeaders();
		headers.add("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuIiwiZXhwIjoyNDc0NTUxMTI1fQ.r6md4OvfY2Crs9IzAu8d8rhInKhHMMig7xn1YUKFO6I4QwmIO1Q5mo5zQXPdaTdvbVZe8JUW2wp784fXEuW8SQ");
		for (Person person2 : person) {
			int id = person2.getId();
			RequestEntity<Void> requestEntity = new RequestEntity<Void>(headers, HttpMethod.GET, new URI("http://localhost:" + this.port + "/person/" + id));
			ResponseEntity<Person> responseEntity = template.exchange(requestEntity, Person.class);
			assertEquals(this.person.get(id), responseEntity.getBody());
		}
	}
	
	@Test
	public void testPost() {
		String login = UUID.randomUUID().toString();
		String password = UUID.randomUUID().toString();
		Person savedPerson = new Person(11, login, password);
		String authority = "ROLE_USER";
		Mockito.when(encoder.encode(savedPerson.getPassword())).thenReturn(password);	
		Mockito.when(roles.findByAuthority(authority)).thenReturn(new Role());
		savedPerson.setRole(this.roles.findByAuthority(authority));
		Mockito.when(repository.save(savedPerson)).thenReturn(savedPerson);
		Person findedPerson = template.postForEntity("/person/", savedPerson, Person.class).getBody();
		assertEquals(findedPerson.getLogin(), savedPerson.getLogin());
		assertEquals(findedPerson.getPassword(), savedPerson.getPassword());
	}
}
