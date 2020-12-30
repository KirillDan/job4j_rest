package ru.job4j.auth.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
		return StreamSupport.stream(
                this.employeeRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
	}
	/**
	 * Найти сотрудника по id.
	 * @param id
	 * @return employee
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Employee> getByIdEmployee(@PathVariable String id) {
		Optional<Employee> optional = this.employeeRepository.findById(Integer.valueOf(id));
		return new ResponseEntity<Employee>(
				optional.orElse(new Employee()),
				optional.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
	}
	/**
	 * Сохранение сотрудника.
	 * @param employee
	 * @return employee
	 */
	@PostMapping
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
		return new ResponseEntity<Employee>(
				this.employeeRepository.save(employee),
				HttpStatus.CREATED);
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
		ResponseEntity<Void> result;
		Optional<Employee> optional = this.employeeRepository.findById(id);
		result = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		if (optional.isPresent()) {
			Employee employee = optional.get();
			RequestEntity requestEntity = new RequestEntity<Person>(person, HttpMethod.POST, new URI(this.API));
			ResponseEntity<Person> savedResponseEntity = this.rest.exchange(requestEntity, new ParameterizedTypeReference<Person>() {});
			Person savedPerson = savedResponseEntity.getBody();
			result = new ResponseEntity<Void>(savedResponseEntity.getStatusCode());
			if (savedPerson != null) {
				employee.addAccount(savedPerson);
				this.employeeRepository.save(employee);
				result = new ResponseEntity<Void>(HttpStatus.CREATED);
			}
		}
		return result;
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
		ResponseEntity<Void> result;	
		ResponseEntity<Person> personOptionalR = this.rest.getForEntity(API_ID, Person.class, person.getId());
		Person personR = personOptionalR.getBody();
		result = new ResponseEntity<Void>(personOptionalR.getStatusCode());	
		if (personR != null) {
			personR.setLogin(person.getLogin());
			personR.setPassword(person.getPassword());
			RequestEntity requestEntity = new RequestEntity<Person>(personR, HttpMethod.PUT, new URI(this.API));
			ResponseEntity<Person> personOptionalR2 = this.rest.exchange(requestEntity, new ParameterizedTypeReference<Person>() {});
			Person personR2 = personOptionalR2.getBody();
			int status = personOptionalR2.getStatusCodeValue();
			if (status >= 200 && status < 300) {
				result = new ResponseEntity<Void>(HttpStatus.CREATED);		
			}
		}
		return result;
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
		ResponseEntity<Void> result;
		this.rest.delete(API_ID, personId);
		
		Optional<Employee> employeeOptional = this.employeeRepository.findById(employeeId);
		result = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);	
		if (employeeOptional.isPresent()) {
			Employee employee = employeeOptional.get();		
			employee.deleteAccount(personId);
			this.employeeRepository.save(employee);
			result = new ResponseEntity<Void>(HttpStatus.CREATED);
		}
		return result;
	}
}
