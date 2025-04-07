package pl.edu.pwr.ztw.books.books;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pwr.ztw.books.authors.Author;


@Getter
@Setter
public class Book {

    private long id;
    private String title;
    private Author author;
    private Integer pages;
    private boolean available;

    public Book(long id, String title, Author author, Integer pages) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.available = true;
    }

}