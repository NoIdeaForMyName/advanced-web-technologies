package pl.edu.pwr.ztw.books.readers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.ztw.books.PaginatedResponse;
import pl.edu.pwr.ztw.books.authors.Author;

@RestController
@RequestMapping("api/readers")
public class ReadersController {

    private final IReadersService readerService;

    public ReadersController(IReadersService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<Reader>> getAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int perPage) {

        PaginatedResponse<Reader> response = readerService.getPaginatedReaders(page, perPage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getReader(@PathVariable long id) {
        Reader reader = readerService.getReader(id);
        if (reader != null) {
            return new ResponseEntity<>(reader, HttpStatus.OK);
        }
        return new ResponseEntity<>("Reader not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Object> addReader(@RequestBody Reader reader) {
        Reader newReader = readerService.addReader(reader);
        if (newReader == null)
            return new ResponseEntity<>("Not all reader fields provided", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(newReader, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateReader(@PathVariable long id, @RequestBody Reader reader) {
        Reader updatedReader = readerService.updateReader(id, reader);
        if (updatedReader != null) {
            return new ResponseEntity<>(updatedReader, HttpStatus.OK);
        }
        return new ResponseEntity<>("Reader not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReader(@PathVariable long id) {
        if (readerService.deleteReader(id)) {
            return new ResponseEntity<>("Reader deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Reader not found", HttpStatus.NOT_FOUND);
    }
}
