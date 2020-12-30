package ru.job4j.auth.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.job4j.auth.repository.MessageRepository;

@RequestMapping("/message")
@RestController
public class MessageController {
	@Autowired
	private MessageRepository repository;
	@Autowired
	private RestTemplate restTemplate;
	private static final String API_PERSON = "http://localhost:8080/person/";
	private static final String API_ID_PERSON = "http://localhost:8080/person/{id}";

	@GetMapping
	public List<Message> getAll() {
		return StreamSupport.stream(
                this.repository.findAll().spliterator(), false
        ).collect(Collectors.toList());
	}

	@PostMapping
	public ResponseEntity<Message> add(@RequestBody Message message, @RequestParam(required = false) String personId) {
		ResponseEntity<Message> result;
		Person person = this.restTemplate.getForObject(API_ID_PERSON, Person.class, personId);
		result = new ResponseEntity<Message>(new Message(), HttpStatus.NOT_FOUND);
		if (person != null) {
			message.setPerson(person);
			person.add(message);
			ResponseEntity<Person> savedPersonEntity = this.restTemplate.postForEntity(API_PERSON, person, Person.class);
			int status = savedPersonEntity.getStatusCodeValue();
			if (status >= 200 && status < 300) {
				Person savedPerson = savedPersonEntity.getBody();
				result = new ResponseEntity<Message>(
		                this.repository.save(message),
		                HttpStatus.CREATED
		        );
			}
		}
		return result;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Message> findById(@PathVariable String id) {
		Optional<Message> optional = this.repository.findById(Integer.valueOf(id));
		return new ResponseEntity<Message>(
				optional.orElse(new Message()),
				optional.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
	}
}
