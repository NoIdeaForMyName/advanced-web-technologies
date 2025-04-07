package pl.edu.pwr.ztw.books.books;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.ztw.books.PaginatedResponse;
import pl.edu.pwr.ztw.books.authors.Author;
import pl.edu.pwr.ztw.books.authors.IAuthorService;
import pl.edu.pwr.ztw.books.rentals.IRentalService;
import pl.edu.pwr.ztw.books.rentals.Rental;

@RestController
@RequestMapping("/api/v1/books")
public class BooksController {

    private final IBooksService booksService;
    private final IAuthorService authorService;
    private final IRentalService rentalService;

    public BooksController(IBooksService booksService, IAuthorService authorService, IRentalService rentalService) {
        this.booksService = booksService;
        this.authorService = authorService;
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<Book>> getAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage) {

        PaginatedResponse<Book> response = booksService.getPaginatedBooks(page, perPage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBook(@PathVariable("id") int id) {
        Book book = booksService.getBook(id);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Object> addBook(@RequestBody CreateBookRequest book) {
        Author newAuthor = authorService.getAuthor(book.getAuthorId());
        Book newBook = booksService.addBook(new Book(0, book.getTitle(), newAuthor, book.getPages()));
        if (newBook == null)
            return new ResponseEntity<>("Not all book fields provided", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable("id") int id, @RequestBody Book book) {
        Book updatedBook = booksService.updateBook(id, book);
        if (updatedBook != null) {
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        }
        return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable("id") int id) {
        Rental boundRental = rentalService.getRentals().stream()
                .filter(r -> r.getBook().getId() == id)
                .findFirst()
                .orElse(null);
        if (boundRental != null) {
            return new ResponseEntity<>("Book is rented", HttpStatus.BAD_REQUEST);
        }
        if (booksService.deleteBook(id)) {
            return new ResponseEntity<>("Book deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
    }
}
