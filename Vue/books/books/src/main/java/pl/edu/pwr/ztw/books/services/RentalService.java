package pl.edu.pwr.ztw.books.services;

import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.models.ActivityLog;
import pl.edu.pwr.ztw.books.models.Book;
import pl.edu.pwr.ztw.books.models.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        new ActivityLog("User " + reader.getName() + " rented '" + book.getTitle() + "'");
        return true;
    }

    public boolean returnBook(Book book) {
        if (book == null || !rentals.containsKey(book)) {
            return false;
        }
        Reader reader = rentals.get(book);
        rentals.remove(book);
        book.setAvailable(true);
        new ActivityLog("User " + reader.getName() + " returned '" + book.getTitle() + "'");
        return true;
    }

    public Map<String, Object> getBookRenterDetails(Book book) {
        Reader reader = rentals.get(book);
        if (reader == null) return null;

        return Map.of(
                "name", reader.getName(),
                "email", reader.getEmail()
        );
    }

    public Reader getBookRenter(Book book) {
        return rentals.get(book);
    }

    public List<Map<String, Object>> getAllRentals() {
        List<Map<String, Object>> result = new ArrayList<>();

        rentals.forEach((book, reader) -> {
            Map<String, Object> bookMap = new HashMap<>();
            bookMap.put("id", book.getId());
            bookMap.put("title", book.getTitle());
            bookMap.put("author", book.getAuthor() != null ? book.getAuthor().getName() + " " + book.getAuthor().getLastName() : "Unknown");
            bookMap.put("available", book.isAvailable());

            Map<String, Object> readerMap = new HashMap<>();
            readerMap.put("name", reader.getName());
            readerMap.put("email", reader.getEmail());

            Map<String, Object> rentalMap = new HashMap<>();
            rentalMap.put("book", bookMap);
            rentalMap.put("reader", readerMap);

            result.add(rentalMap);
        });

        return result;
    }
}