package pl.edu.pwr.ztw.books.rentals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.ztw.books.books.Book;
import pl.edu.pwr.ztw.books.readers.Reader;
import pl.edu.pwr.ztw.books.books.IBooksService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @Autowired
    private IBooksService booksService;

    @PostMapping("/rent")
    public ResponseEntity<Object> rentBook(@RequestParam int bookId, @RequestBody Reader reader) {
        Book book = booksService.getBook(bookId);
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        if (rentalService.rentBook(book, reader)) {
            return new ResponseEntity<>("Book rented successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book is not available", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/return/{bookId}")
    public ResponseEntity<Object> returnBook(@PathVariable int bookId) {
        Book book = booksService.getBook(bookId);
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        if (rentalService.returnBook(book)) {
            return new ResponseEntity<>("Book returned successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book was not rented", HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRentals() {
        return new ResponseEntity<>(rentalService.getAllRentals(), HttpStatus.OK);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Object> getBookRenter(@PathVariable int bookId) {
        Book book = booksService.getBook(bookId);
        if (book == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        Reader renter = rentalService.getBookRenter(book);
        if (renter != null) {
            return new ResponseEntity<>(renter, HttpStatus.OK);
        }
        return new ResponseEntity<>("Book is not rented", HttpStatus.NOT_FOUND);
    }
}