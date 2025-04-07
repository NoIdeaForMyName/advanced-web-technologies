package pl.edu.pwr.ztw.books.rentals;

import pl.edu.pwr.ztw.books.PaginatedResponse;
import pl.edu.pwr.ztw.books.authors.Author;
import pl.edu.pwr.ztw.books.books.Book;
import pl.edu.pwr.ztw.books.readers.Reader;

import java.util.Collection;

public interface IRentalService {
    Rental createRental(Reader reader, Book book);
    Collection<Rental> getRentals();
    PaginatedResponse<Rental> getPaginatedRentals(int page, int size);
    Rental getRental(long id);
    Reader getBookRenter(long id);
    Rental rentBook(Reader reader, Book book);
    Rental endRental(long id);
    Rental updateRental(long id, Rental rental);
    boolean deleteRental(long id);
}
