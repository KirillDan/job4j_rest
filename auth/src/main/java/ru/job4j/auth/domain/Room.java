package ru.job4j.auth.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Room {
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String description;
	private Timestamp time;

	@OneToOne
	@JoinColumn(name = "person_creater")
	private Person personWhoCreateRoom;
	@OneToMany
	@JoinTable(name = "room_messages", joinColumns = { @JoinColumn(name = "room_id") }, inverseJoinColumns = {
			@JoinColumn(name = "messages_id") })
	private List<Message> messages;
	
	public static Room of(int id, String name, String description) {
		Room room = new Room();
		room.id = id;
		room.name = name;
		room.description = description;
		room.time = Timestamp.valueOf(LocalDateTime.now());
		return room;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public Person getPersonWhoCreateRoom() {
		return personWhoCreateRoom;
	}
	
	public void setPersonWhoCreateRoom(Person personWhoCreateRoom) {
		this.personWhoCreateRoom = personWhoCreateRoom;
	}
	
	public void add(Message message) {
    	if (this.messages == null) {
    		this.messages = new ArrayList<Message>();
    	}
    	this.messages.add(message);
    }
    
    public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", name=" + name + ", description=" + description + ", time=" + time
				+ ", personWhoCreateRoom=" + personWhoCreateRoom + ", messages=" + messages + "]";
	}
}
