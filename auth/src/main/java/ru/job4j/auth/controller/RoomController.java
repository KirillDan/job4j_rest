package ru.job4j.auth.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ru.job4j.auth.domain.Message;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.domain.Room;
import ru.job4j.auth.repository.RoomRepository;

@RequestMapping("/room")
@RestController
public class RoomController {
	@Autowired
	private RoomRepository repository;
	@Autowired
	private RestTemplate restTemplate;
	private static final String API_ID_PERSON = "http://localhost:8080/person/{id}";
	private static final String API_ID_MESSAGE = "http://localhost:8080/message/{id}";
	
	@GetMapping
	public List<Room> getAll() {
		return StreamSupport.stream(
                this.repository.findAll().spliterator(), false
        ).collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Room> getById(@PathVariable Integer id) {
		Optional<Room> optional = this.repository.findById(id);
		return new ResponseEntity<Room>(
				optional.orElse(new Room()),
				optional.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
	}
	
	@PostMapping
	public ResponseEntity<Room> create(@RequestBody Room room, @RequestParam String personId) {
		ResponseEntity<Room> result;
		Person person = this.restTemplate.getForObject(API_ID_PERSON, Person.class, personId);
		result = new ResponseEntity<Room>(
				new Room(),
                HttpStatus.NOT_FOUND
        );
		if (person != null) {
			room.setPersonWhoCreateRoom(person);
			result = new ResponseEntity<Room>(
					this.repository.save(room),
	                HttpStatus.CREATED
	        );
		}
		return result;
	}
	
	@PostMapping("/insert")
	public ResponseEntity<Void> insertMessageIntoRoom(@RequestParam String messageId, @RequestParam String roomId) {
		ResponseEntity<Void> result;
		Message message = this.restTemplate.getForObject(API_ID_MESSAGE, Message.class, messageId);
		result = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		if (message != null) {
			Optional<Room> roomOptional = this.repository.findById(Integer.valueOf(roomId));
			if (roomOptional.isPresent()) {
				Room room = roomOptional.get();
				room.add(message);
				this.repository.save(room);
				result = new ResponseEntity<Void>(HttpStatus.OK);
			}
		}
		return result;
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Integer id) {
		this.repository.deleteById(id);
	}
}
