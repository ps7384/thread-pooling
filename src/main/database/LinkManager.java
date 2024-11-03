package main.database;


import java.util.ArrayList;
import java.util.List;

public class LinkManager {
    private final List<String> bookNames;

    public LinkManager() {
        this.bookNames = new ArrayList<>();
    }

    public void addBookNames(List<String> names) {
        synchronized (bookNames) {
            bookNames.addAll(names);
        }
    }

    public List<String> getBookNames() {
        return bookNames;
    }
}

