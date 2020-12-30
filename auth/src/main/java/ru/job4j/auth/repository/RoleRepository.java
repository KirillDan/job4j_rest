package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;

import ru.job4j.auth.domain.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
