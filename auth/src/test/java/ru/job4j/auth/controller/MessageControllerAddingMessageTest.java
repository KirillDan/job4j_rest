package ru.job4j.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ru.job4j.auth.domain.Message;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.MessageRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MessageControllerAddingMessageTest {
	@Autowired
	private TestRestTemplate template;
	@LocalServerPort
	private int port;
	@MockBean
	private MessageRepository repository;
	@MockBean
	private RestTemplate restTemplate;

	private Message message;
	
	@BeforeEach
	private void setup() {
		this.message = Message.of(1, "text");
		Mockito.when(repository.save(Mockito.any(Message.class))).thenReturn(message);
		Person person = new Person(1, "login", "ps");
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(), Mockito.eq("1"))).thenReturn(person);
		Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.eq(Person.class)))
		.thenReturn(new ResponseEntity<Person>(person, HttpStatus.CREATED));
	}

	@AfterEach
	private void end() {
		this.repository.deleteAll();
	}

	@Test
	public void testAddMessage() throws URISyntaxException {
		RequestEntity<Message> msgR = new RequestEntity<Message>(message, HttpMethod.POST,
				new URI("http://localhost:" + this.port + "/message?personId=1"));
		ResponseEntity<Message> responseMessageEntity = template.exchange(msgR, Message.class);
		Message responseMessage = responseMessageEntity.getBody();
		assertEquals(responseMessageEntity.getStatusCode(), HttpStatus.CREATED);
		assertEquals(responseMessage.getId(), message.getId());
		assertEquals(responseMessage.getText(), message.getText());
		assertEquals(responseMessage.getTime(), message.getTime());
	}
}
