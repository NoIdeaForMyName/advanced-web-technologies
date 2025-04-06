package pl.edu.pwr.ztw.books.authors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
public class AuthorsController {

    private final IAuthorService authorService;

    public AuthorsController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<Object> getAuthors() {
        return new ResponseEntity<>(authorService.getAuthors(), HttpStatus.OK);
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
        return new ResponseEntity<>(authorService.addAuthor(author), HttpStatus.CREATED);
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
        if (authorService.deleteAuthor(id)) {
            return new ResponseEntity<>("Author deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
    }
}