package pl.edu.pwr.ztw.books.services;

import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.models.Book;
import pl.edu.pwr.ztw.books.models.Reader;

import java.util.HashMap;
import java.util.Map;

@Service
public class RentalService {
    private final Map<Book, Reader> rentals = new HashMap<>();

    public boolean rentBook(Book book, Reader reader) {
        if (book == null || reader == null || !book.isAvailable()) {
            return false;
        }
        rentals.put(book, reader);
        book.setAvailable(false);
        return true;
    }

    public boolean returnBook(Book book) {
        if (book == null || !rentals.containsKey(book)) {
            return false;
        }
        rentals.remove(book);
        book.setAvailable(true);
        return true;
    }

    public Reader getBookRenter(Book book) {
        return rentals.get(book);
    }

    public Map<Book, Reader> getAllRentals() {
        return new HashMap<>(rentals);
    }
}