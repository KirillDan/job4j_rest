package ru.job4j.auth.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.EmployeeRepository;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	private RestTemplate rest;
	@Autowired
	private EmployeeRepository employeeRepository;
	private static final String API = "http://localhost:8080/person/";
	private static final String API_ID = "http://localhost:8080/person/{id}";
	
	/**
	 * Получение всех сотрудников со всеми их аккаунтами.
	 * @return List<Employee>
	 */
	@GetMapping("/")
	public List<Employee> getAll() {
		return this.employeeRepository.findAll();
	}
	/**
	 * Добавление нового аккаунта.
	 * @param person
	 * @param id
	 * @return void
	 * @throws URISyntaxException
	 */
	@PostMapping("/{id}")
	public ResponseEntity<Void> add(@RequestBody Person person, @PathVariable Integer id) throws URISyntaxException {
		Employee employee = this.employeeRepository.findById(id).get();
		RequestEntity requestEntity = new RequestEntity<Person>(person, HttpMethod.POST, new URI(this.API));
		Person savedPerson = this.rest.exchange(requestEntity, new ParameterizedTypeReference<Person>() {}).getBody();
		employee.addAccount(savedPerson);
		this.employeeRepository.save(employee);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Изменение существующих аккаунтов.
	 * @param person
	 * @param id
	 * @return void
	 * @throws URISyntaxException
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Void> put(@RequestBody Person person) throws URISyntaxException {
		Person personR = this.rest.getForEntity(API_ID, Person.class, person.getId()).getBody();
		personR.setLogin(person.getLogin());
		personR.setPassword(person.getPassword());
		RequestEntity requestEntity = new RequestEntity<Person>(personR, HttpMethod.PUT, new URI(this.API));
		Person personR2 = this.rest.exchange(requestEntity, new ParameterizedTypeReference<Person>() {}).getBody();
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Удаление существующих аккаунтов.
	 * @param personId
	 * @param employeeId
	 * @return void
	 * @throws URISyntaxException
	 */
	@DeleteMapping("/{personId}/{employeeId}")
	public ResponseEntity<Void> delete(@PathVariable Integer personId, @PathVariable Integer employeeId) throws URISyntaxException {
		this.rest.delete(API_ID, personId);
		Employee employee = this.employeeRepository.findById(employeeId).get();
		employee.deleteAccount(personId);
		this.employeeRepository.save(employee);
		return ResponseEntity.ok().build();
	}
}
