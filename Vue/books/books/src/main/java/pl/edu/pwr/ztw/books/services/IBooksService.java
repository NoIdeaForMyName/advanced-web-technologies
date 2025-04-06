package pl.edu.pwr.ztw.books.services;

import pl.edu.pwr.ztw.books.models.Book;
import java.util.Collection;

public interface IBooksService {
    Collection<Book> getBooks();
    Book getBook(int id);
    Book addBook(Book book);
    Book updateBook(int id, Book book);
    boolean deleteBook(int id);
}