package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;

import ru.job4j.auth.domain.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {
}
