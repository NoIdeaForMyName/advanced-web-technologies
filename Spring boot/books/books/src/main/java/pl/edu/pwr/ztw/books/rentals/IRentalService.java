package pl.edu.pwr.ztw.books.rentals;

import pl.edu.pwr.ztw.books.books.Book;
import pl.edu.pwr.ztw.books.readers.Reader;

import java.util.Collection;

public interface IRentalService {
    Rental createRental(Reader reader, Book book);
    Collection<Rental> getRentals();
    Rental getRental(long id);
    Reader getBookRenter(long id);
    Rental addRental(Rental rental);
    Rental endRental(long id);
    Rental updateRental(long id, Rental rental);
    boolean deleteRental(long id);
}
