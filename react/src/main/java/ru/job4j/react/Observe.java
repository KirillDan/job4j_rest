package ru.job4j.react;

public interface Observe<T> {
	void receive(T model);
}