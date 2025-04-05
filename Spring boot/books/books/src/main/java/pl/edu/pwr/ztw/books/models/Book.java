package pl.edu.pwr.ztw.books.models;

public class Book {
    private final int id;
    private String title;
    private Author author;
    private int pages;
    private boolean available;

    public Book(int id, String title, Author author, int pages) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.available = true;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}