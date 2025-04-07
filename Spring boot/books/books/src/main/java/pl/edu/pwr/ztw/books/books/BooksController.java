package pl.edu.pwr.ztw.books.books;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BooksController {

    private final IBooksService booksService;

    public BooksController(IBooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping
    public ResponseEntity<Object> getBooks() {
        return new ResponseEntity<>(booksService.getBooks(), HttpStatus.OK);
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
    public ResponseEntity<Object> addBook(@RequestBody Book book) {
        Book newBook = booksService.addBook(book);
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
        if (booksService.deleteBook(id)) {
            return new ResponseEntity<>("Book deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
    }
}