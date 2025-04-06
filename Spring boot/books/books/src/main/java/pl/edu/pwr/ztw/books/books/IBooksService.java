package pl.edu.pwr.ztw.books.books;

import java.util.Collection;

public interface IBooksService {
    Collection<Book> getBooks();
    Book getBook(long id);
    Book addBook(Book book);
    Book updateBook(long id, Book book);
    boolean deleteBook(long id);
}
