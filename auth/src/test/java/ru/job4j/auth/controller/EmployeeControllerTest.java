package ru.job4j.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {
	@Autowired
	private TestRestTemplate template;
	@LocalServerPort
	private int port;
	@MockBean
	private EmployeeRepository repository;

	private List<Employee> emoloyees;

	@BeforeEach
	private void setup() {
		this.emoloyees = new ArrayList();
		for (int i = 0; i < 10; i++) {
			this.emoloyees.add(Employee.of(i, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
					UUID.randomUUID().toString(), null));
			Mockito.when(repository.findById(i)).thenReturn(Optional.of(this.emoloyees.get(i)));
		}
		Mockito.when(repository.findAll()).thenReturn(this.emoloyees);
		for (int i = 0; i < 10; i++) {
			Mockito.when(repository.save(Mockito.refEq(this.emoloyees.get(i), "id"))).thenReturn(this.emoloyees.get(i));
		}
	}

	@AfterEach
	private void end() {
		this.repository.deleteAll();
	}

	@Test
	public void testFindAll() {
		ResponseEntity<Employee[]> responseEntity = template.getForEntity("/employee/", Employee[].class);
		List<Object> responseEmployees = Arrays.asList(responseEntity.getBody());
		for (Employee employee : emoloyees) {
			Employee empItr = (Employee) responseEmployees.get(employee.getId());
			assertEquals(employee.getId(), empItr.getId());
		}
	}

	@Test
	public void testFindById() {
		for (int i = 0; i < this.emoloyees.size(); i++) {
			Employee responseEmployee = template.getForObject("/employee/{id}", Employee.class, i);
			assertEquals(responseEmployee.getId(), this.emoloyees.get(i).getId());
			assertEquals(responseEmployee.getInn(), this.emoloyees.get(i).getInn());
			assertEquals(responseEmployee.getName(), this.emoloyees.get(i).getName());
			assertEquals(responseEmployee.getSecondname(), this.emoloyees.get(i).getSecondname());
		}
	}

	@Test
	public void testAddEmployee() throws URISyntaxException {
		for (int i = 0; i < this.emoloyees.size(); i++) {
			RequestEntity<Employee> empR = new RequestEntity<Employee>(this.emoloyees.get(i), HttpMethod.POST,
					new URI("http://localhost:" + this.port + "/employee"));
			Employee responseEmployee = template.exchange(empR, Employee.class).getBody();
			assertEquals(responseEmployee.getId(), this.emoloyees.get(i).getId());
			assertEquals(responseEmployee.getInn(), this.emoloyees.get(i).getInn());
			assertEquals(responseEmployee.getName(), this.emoloyees.get(i).getName());
			assertEquals(responseEmployee.getSecondname(), this.emoloyees.get(i).getSecondname());
		}
	}
}
