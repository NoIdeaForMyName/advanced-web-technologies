package pl.edu.pwr.ztw.books.rentals;

import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.ztw.books.books.Book;
import pl.edu.pwr.ztw.books.books.BooksService;
import pl.edu.pwr.ztw.books.readers.Reader;
import pl.edu.pwr.ztw.books.books.IBooksService;
import pl.edu.pwr.ztw.books.readers.ReadersService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final ReadersService readersService;
    private final IBooksService booksService;

    public RentalController(RentalService rentalService, ReadersService readersService, BooksService booksService) {
        this.rentalService = rentalService;
        this.readersService = readersService;
        this.booksService = booksService;
    }

    @GetMapping
    public ResponseEntity<Object> getRentals() {
        return new ResponseEntity<>(rentalService.getRentals(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRental(@PathVariable long id) {
        Rental rental = rentalService.getRental(id);
        if (rental != null) {
            return new ResponseEntity<>(rental, HttpStatus.OK);
        }
        return new ResponseEntity<>("Rental not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/rent")
    public ResponseEntity<Object> rentBook(@RequestBody CreateRentalRequest createRentalRequest) {
        Reader reader = readersService.getReader(createRentalRequest.getReaderId());
        if (reader == null) {
            return new ResponseEntity<>("Reader not found", HttpStatus.NOT_FOUND);
        }
        Book book = booksService.getBook(createRentalRequest.getBookId());
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        if (rentalService.rentBook(reader, book) != null) {
            return new ResponseEntity<>("Book rented successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book is not available", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/return/{rentalId}")
    public ResponseEntity<Object> returnBook(@PathVariable int rentalId) {
        Rental rental = rentalService.endRental(rentalId);
        if (rental == null) {
            return new ResponseEntity<>("Rental not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Rental ended successfully", HttpStatus.OK);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Object> getBookRenter(@PathVariable int bookId) {
        Book book = booksService.getBook(bookId);
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        Reader renter = rentalService.getBookRenter(bookId);
        if (renter != null) {
            return new ResponseEntity<>(renter, HttpStatus.OK);
        }
        return new ResponseEntity<>("Book is not rented", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRental(@PathVariable long id, @RequestBody UpdateRentalRequest updateRentalRequest) {
        Reader reader = null;
        Book book = null;
        Rental currRental = rentalService.getRental(id);
        if (updateRentalRequest.getReaderId() != null) {
            reader = readersService.getReader(updateRentalRequest.getReaderId());
            if (reader == null) {
                return new ResponseEntity<>("Reader not found", HttpStatus.NOT_FOUND);
            }
        }
        if (updateRentalRequest.getBookId() != null) {
            book = booksService.getBook(updateRentalRequest.getBookId());
            if (book == null) {
                return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
            }
            if (currRental != null && currRental.getBook() != book && !book.isAvailable() && updateRentalRequest.getReturnDate() == null)
                return new ResponseEntity<>("Book is not available", HttpStatus.BAD_REQUEST);
        }
        Rental updatedRental  = new Rental(0, reader, book);
        updatedRental.setRentalDate(updateRentalRequest.getRentalDate());
        updatedRental.setReturnDate(updateRentalRequest.getReturnDate());
        if (rentalService.updateRental(id, updatedRental) != null) {
            return new ResponseEntity<>(currRental, HttpStatus.OK);
        }
        return new ResponseEntity<>("Rental not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRental(@PathVariable("id") int id) {
        if (rentalService.deleteRental(id)) {
            return new ResponseEntity<>("Rental deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Rental not found", HttpStatus.NOT_FOUND);
    }

}
