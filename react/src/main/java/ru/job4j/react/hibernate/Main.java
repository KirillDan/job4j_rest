package ru.job4j.react.hibernate;

import java.sql.Timestamp;

import ru.job4j.react.hibernate.entity.Item;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		HbmTracker hbmTracker = new HbmTracker();
		for (int i = 0; i < 10; i++) {
			hbmTracker.add(new Item("name" + i, "description" + 1, new Timestamp(1459510232000L)));
		}
		hbmTracker.findAll(System.out::println);
		
	}

}
