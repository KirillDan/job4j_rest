package ru.job4j.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.job4j.auth.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
