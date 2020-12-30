package ru.job4j.auth.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Message {
	@Id
	@GeneratedValue
	private int id;
	private String text;
	private Timestamp time;
	
	@JsonIgnore
	@ManyToOne
	private Person person;
	
	public static Message of(int id, String text) {
		Message message = new Message();
		message.id = id;
		message.text = text;
        message.time = new Timestamp(System.currentTimeMillis());
        return message;
    }
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", text=" + text + ", time=" + time + ", person=" + person + "]";
	}
	
}
