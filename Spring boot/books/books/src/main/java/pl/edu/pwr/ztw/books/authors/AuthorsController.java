package pl.edu.pwr.ztw.books.authors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.ztw.books.PaginatedResponse;
import pl.edu.pwr.ztw.books.books.Book;
import pl.edu.pwr.ztw.books.books.IBooksService;
import pl.edu.pwr.ztw.books.rentals.Rental;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorsController {

    private final IAuthorService authorService;
    private final IBooksService booksService;

    public AuthorsController(IAuthorService authorService, IBooksService booksService) {
        this.authorService = authorService;
        this.booksService = booksService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<Author>> getAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage) {

        PaginatedResponse<Author> response = authorService.getPaginatedAuthors(page, perPage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAuthor(@PathVariable long id) {
        Author author = authorService.getAuthor(id);
        if (author != null) {
            return new ResponseEntity<>(author, HttpStatus.OK);
        }
        return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Object> addAuthor(@RequestBody Author author) {
        Author newAuthor = authorService.addAuthor(author);
        if (newAuthor == null)
            return new ResponseEntity<>("Not all author fields provided", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(newAuthor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAuthor(@PathVariable long id, @RequestBody Author author) {
        Author updatedAuthor = authorService.updateAuthor(id, author);
        if (updatedAuthor != null) {
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
        }
        return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAuthor(@PathVariable long id) {
        Book boundBook = booksService.getBooks().stream()
                .filter(b -> b.getAuthor().getId() == id)
                .findFirst()
                .orElse(null);
        if (boundBook != null) {
            return new ResponseEntity<>("Author is bounded to book", HttpStatus.BAD_REQUEST);
        }
        if (authorService.deleteAuthor(id)) {
            return new ResponseEntity<>("Author deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
    }
}
