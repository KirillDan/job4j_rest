package ru.job4j.react;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private List<String> data = List.of("first", "second", "third");

    public void getByReact(Observe<String> observe) throws InterruptedException {
        for (String datum : data) {
            Thread.sleep(1000);
            observe.receive(datum);
        }
    }

    public List<String> get() throws InterruptedException {
        List<String> rsl = new ArrayList<>();
        for (String datum : data) {
            Thread.sleep(1000);
            rsl.add(datum);
        }
        return rsl;
    }
}
