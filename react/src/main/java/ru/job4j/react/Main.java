package ru.job4j.react;

import java.util.List;

public class Main {
//    public static void main(String[] args) throws InterruptedException {
//        var store = new Store();
//        List<String> data = store.get();
//        for (String datum : data) {
//            System.out.println(datum);
//        }
//    }
    
    public static void main(String[] args) throws InterruptedException {
        Store store = new Store();
        store.getByReact(System.out::println);
    }
}
