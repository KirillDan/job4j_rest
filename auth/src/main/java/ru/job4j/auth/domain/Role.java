package ru.job4j.auth.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Role {
	@Id
	@GeneratedValue
	private int id;
	private String authority;

	@ManyToOne
	private Person person;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

	@Override
	public String toString() {
		return "Role [id=" + id + ", authority=" + authority + ", person=" + person + "]";
	}
}
