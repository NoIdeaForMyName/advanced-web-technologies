package pl.edu.pwr.ztw.books.rentals;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pwr.ztw.books.books.Book;
import pl.edu.pwr.ztw.books.readers.Reader;

import java.time.LocalDateTime;

@Getter
@Setter
public class Rental {

    public Rental(long id, Reader reader, Book book) {
        this.id = id;
        this.reader = reader;
        this.book = book;
        this.rentalDate = LocalDateTime.now();

        //this.book.setAvailable(false);
    }

    private long id;
    private Reader reader;
    private Book book;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;

}
