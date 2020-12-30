package ru.job4j.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

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
import org.springframework.web.client.RestTemplate;

import ru.job4j.auth.domain.Message;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.domain.Room;
import ru.job4j.auth.repository.RoomRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoomControllerAddingRoomAndCreatingMessageTest {
	@Autowired
	private TestRestTemplate template;
	@LocalServerPort
	private int port;
	@MockBean
	private RoomRepository repository;
	@MockBean
	private RestTemplate restTemplate;

	private Room room;
	private Person person;
	private Message message;
	
	@BeforeEach
	private void setup() {
		this.room = Room.of(1, "name", "description");
		person = new Person(1, "login", "ps");
		this.message = Message.of(1, "text");
		room.setPersonWhoCreateRoom(person);
		Mockito.when(repository.save(Mockito.any(Room.class))).thenReturn(room);
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(room));
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Person.class), Mockito.eq("1"))).thenReturn(person);
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Message.class), Mockito.eq("1"))).thenReturn(message);
	}

	@AfterEach
	private void end() {
		this.repository.deleteAll();
	}

	@Test
	public void testAddRoom() throws URISyntaxException {
		RequestEntity<Room> roomR = new RequestEntity<Room>(room, HttpMethod.POST,
				new URI("http://localhost:" + this.port + "/room?personId=1"));
		Room responseRoom = template.exchange(roomR, Room.class).getBody();
		assertEquals(responseRoom.getId(), this.room.getId());
		assertEquals(responseRoom.getName(), this.room.getName());
		assertEquals(responseRoom.getTime().toGMTString(), this.room.getTime().toGMTString());
		assertEquals(responseRoom.getDescription(), this.room.getDescription());
		assertEquals(responseRoom.getPersonWhoCreateRoom().getId(), this.person.getId());
		assertEquals(responseRoom.getPersonWhoCreateRoom().getLogin(), this.person.getLogin());
		assertEquals(responseRoom.getPersonWhoCreateRoom().getPassword(), this.person.getPassword());
	}
	
	@Test
	public void testAddMessageIntoRoom() throws URISyntaxException {
		HttpStatus status = template.postForEntity(new URI("http://localhost:" + this.port + "/room/insert?messageId=1&roomId=1"), null, Void.class)
		.getStatusCode();
		assertEquals(status, HttpStatus.OK);
	}
}
