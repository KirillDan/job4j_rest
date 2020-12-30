package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;

import ru.job4j.auth.domain.Room;

public interface RoomRepository extends CrudRepository<Room, Integer> {
}
