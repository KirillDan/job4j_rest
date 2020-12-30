package ru.job4j.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import ru.job4j.auth.domain.Room;
import ru.job4j.auth.repository.RoomRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {
	@Autowired
	private TestRestTemplate template;
	@LocalServerPort
	private int port;
	@MockBean
	private RoomRepository repository;

	private List<Room> rooms;

	@BeforeEach
	private void setup() {
		this.rooms = new ArrayList();
		for (int i = 0; i < 10; i++) {
			this.rooms.add(Room.of(i, UUID.randomUUID().toString(), UUID.randomUUID().toString()));
			Mockito.when(repository.findById(i)).thenReturn(Optional.of(this.rooms.get(i)));
		}
		Mockito.when(repository.findAll()).thenReturn(this.rooms);
	}
	
	@Test
	public void testFindAll() {
		ResponseEntity<Room[]> responseEntity = template.getForEntity("/room/", Room[].class);
		List<Object> responseRooms = Arrays.asList(responseEntity.getBody());
		for (Room room : rooms) {
			Room roomItr = (Room) responseRooms.get(room.getId());
			assertEquals(room.getId(), roomItr.getId());
		}
	}
	
	@Test
	public void testFindById() {
		for (int i = 0; i < this.rooms.size(); i++) {
			Room responseRoom = template.getForObject("/room/{id}", Room.class, i);
			assertEquals(responseRoom.getId(), this.rooms.get(i).getId());
			assertEquals(responseRoom.getName(), this.rooms.get(i).getName());
			assertEquals(responseRoom.getTime().toGMTString(), this.rooms.get(i).getTime().toGMTString());
			assertEquals(responseRoom.getDescription(), this.rooms.get(i).getDescription());
		}	
	}
}
