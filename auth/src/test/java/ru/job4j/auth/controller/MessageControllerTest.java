package ru.job4j.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import org.springframework.http.ResponseEntity;

import ru.job4j.auth.domain.Message;
import ru.job4j.auth.repository.MessageRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MessageControllerTest {
	@Autowired
    private TestRestTemplate template;
	@LocalServerPort
	private int port;
	@MockBean
	private MessageRepository repository;
	
	private List<Message> messages;
	
	@BeforeEach
	private void setup() {
		this.messages = new ArrayList();
		for (int i = 0; i < 10; i++) {
			this.messages.add(Message.of(i, UUID.randomUUID().toString()));
			Mockito.when(repository.findById(i)).thenReturn(Optional.of(this.messages.get(i)));
		}
		Mockito.when(repository.findAll()).thenReturn(this.messages);
		for (int i = 0; i < 10; i++) {
			Mockito.when(repository.save(Mockito.refEq(this.messages.get(i), "id"))).thenReturn(this.messages.get(i));
		}
	}
	
	@AfterEach
	private void end() {
		this.repository.deleteAll();
	}
	
	@Test
	public void testFindAll() {
		ResponseEntity<Message[]> responseEntity = template.getForEntity("/message/", Message[].class);
		List<Object> responseMessages = Arrays.asList(responseEntity.getBody());
		for (Message message : messages) {
			Message messageItr = (Message) responseMessages.get(message.getId());
			assertEquals(message.getId(), messageItr.getId());
			assertEquals(message.getText(), messageItr.getText());
			assertEquals(message.getTime(), messageItr.getTime());
		}
	}
	
	@Test
	public void testFindById() {
		for (int i = 0; i < this.messages.size(); i++) {
			Message responseMessage = template.getForObject("/message/{id}", Message.class, i);
			assertEquals(responseMessage.getId(), this.messages.get(i).getId());
			assertEquals(responseMessage.getText(), this.messages.get(i).getText());
			assertEquals(responseMessage.getTime(), this.messages.get(i).getTime());
		}	
	}
}
