package ru.job4j.react.hibernate;

import java.util.List;

import ru.job4j.react.hibernate.entity.Item;

public interface Store {
	Item add(Item item);

	boolean replace(String id, Item item);

	boolean delete(String id);

//    List<Item> findAll();
	List<Item> findByName(String key);

	Item findById(String id);
}
