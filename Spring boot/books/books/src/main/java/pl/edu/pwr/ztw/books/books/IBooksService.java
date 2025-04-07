package pl.edu.pwr.ztw.books.books;

import pl.edu.pwr.ztw.books.PaginatedResponse;
import pl.edu.pwr.ztw.books.authors.Author;

import java.util.Collection;

public interface IBooksService {
    Collection<Book> getBooks();
    PaginatedResponse<Book> getPaginatedBooks(int page, int size);
    Book getBook(long id);
    Book addBook(Book book);
    Book updateBook(long id, Book book);
    boolean deleteBook(long id);
}
