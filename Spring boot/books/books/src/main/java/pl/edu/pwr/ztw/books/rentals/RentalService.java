package pl.edu.pwr.ztw.books.rentals;

import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.books.Book;
import pl.edu.pwr.ztw.books.books.BooksService;
import pl.edu.pwr.ztw.books.readers.Reader;
import pl.edu.pwr.ztw.books.readers.ReadersService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService implements IRentalService {
    private final List<Rental> rentals = new ArrayList<>();
    private static long nextId = 1;

    public RentalService(ReadersService readerService, BooksService booksService) {
        rentals.add(createRental(readerService.getReader(1), booksService.getBook(1)));
        rentals.add(createRental(readerService.getReader(2), booksService.getBook(2)));
        rentals.add(createRental(readerService.getReader(3), booksService.getBook(3)));
    }

    @Override
    public Rental createRental(Reader reader, Book book) {
        if (!book.isAvailable())
            return null;
        book.setAvailable(false);
        return new Rental(nextId++, reader, book);
    }

    @Override
    public List<Rental> getRentals() {
        return rentals;
    }

    @Override
    public Rental getRental(long id) {
        return rentals.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Reader getBookRenter(long id) {
        return rentals.stream()
                .filter(r -> r.getBook().getId() == id && r.getReturnDate() == null)
                .findFirst()
                .map(Rental::getReader)
                .orElse(null);
    }

    @Override
    public Rental rentBook(Reader reader, Book book) {
        if (!book.isAvailable())
            return null;
        Rental newRental = createRental(reader, book);
//        if (rental.getBook() == null || rental.getReader() == null || !rental.getBook().isAvailable()) {
//            return null;
//        }
        rentals.add(newRental);
        book.setAvailable(false);
        return newRental;
    }

    @Override
    public Rental endRental(long id) {
        Rental toEndRental = getRental(id);
        if (toEndRental != null) {
            toEndRental.setReturnDate(LocalDateTime.now());
            toEndRental.getBook().setAvailable(true);
        }
        return toEndRental;
    }

    @Override
    public Rental updateRental(long id, Rental rental) {
        Rental existingRental = getRental(id);
        if (existingRental != null) {
            existingRental.setReader(rental.getReader() != null ? rental.getReader() : existingRental.getReader());
            if (rental.getBook() != null) {
                existingRental.getBook().setAvailable(true);
                rental.getBook().setAvailable(false);
            }
            existingRental.setBook(rental.getBook() != null ? rental.getBook() : existingRental.getBook());
            existingRental.setRentalDate(rental.getRentalDate() != null ? rental.getRentalDate() : existingRental.getRentalDate());
            existingRental.setReturnDate(rental.getReturnDate());

            if (existingRental.getReturnDate() != null)
                existingRental.getBook().setAvailable(true);
        }
        return existingRental;
    }

    @Override
    public boolean deleteRental(long id) {
        return rentals.removeIf(r -> r.getId() == id);
    }

//    public boolean rentBook(Book book, Reader reader) {
//        if (book == null || reader == null || !book.isAvailable()) {
//            return false;
//        }
//        rentals.put(book, reader);
//        book.setAvailable(false);
//        return true;
//    }
//
//    public boolean returnBook(Book book) {
//        if (book == null || !rentals.containsKey(book)) {
//            return false;
//        }
//        rentals.remove(book);
//        book.setAvailable(true);
//        return true;
//    }
//
//    public Reader getBookRenter(Book book) {
//        return rentals.get(book);
//    }
//
//    public Map<Book, Reader> getAllRentals() {
//        return new HashMap<>(rentals);
//    }
}