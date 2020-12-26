package ru.job4j.auth.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Employee {
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String secondname;
	private String inn;
	private Timestamp dateEmp;
	@OneToMany
	private List<Person> accounts;
	
	public static Employee of(int id, String name, String secondname, String inn, List<Person> persons) {
		Employee employee = new Employee();
		employee.id = id;
		employee.name = name;
		employee.secondname = secondname;
		employee.inn = inn;
		employee.dateEmp = new Timestamp(System.currentTimeMillis());
		employee.accounts = persons;
        return employee;
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

	public String getSecondname() {
		return secondname;
	}

	public void setSecondname(String secondname) {
		this.secondname = secondname;
	}

	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public Timestamp getDateEmp() {
		return dateEmp;
	}

	public void setDateEmp(Timestamp dateEmp) {
		this.dateEmp = dateEmp;
	}

	public List<Person> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Person> accounts) {
		this.accounts = accounts;
	}
	
	public void addAccount(Person account) {
		if (this.accounts == null) {
			this.accounts = new ArrayList<Person>();
		}
		this.accounts.add(account);
	}
	
	public void deleteAccount(Person account) {
		if (this.accounts != null) {
			this.accounts.remove(account.getId());
		}
	}
	
	public void deleteAccount(int accountId) {
		if (this.accounts != null) {
			this.accounts.remove(accountId);
		}
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", secondname=" + secondname + ", inn=" + inn + ", dateEmp="
				+ dateEmp + ", accounts=" + accounts + "]";
	}
}
