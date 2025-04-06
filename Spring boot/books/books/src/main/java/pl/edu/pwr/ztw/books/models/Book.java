package pl.edu.pwr.ztw.books.models;

import lombok.Getter;
import lombok.Setter;


@Getter
public class Book {

    private final int id;

    @Setter
    private String title;

    @Setter
    private Author author;

    @Setter
    private int pages;

    @Setter
    private boolean available;

    public Book(int id, String title, Author author, int pages) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.available = true;
    }

}