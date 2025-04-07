package pl.edu.pwr.ztw.books.readers;

import pl.edu.pwr.ztw.books.PaginatedResponse;
import pl.edu.pwr.ztw.books.authors.Author;

import java.util.Collection;

public interface IReadersService {
    Collection<Reader> getReaders();
    PaginatedResponse<Reader> getPaginatedReaders(int page, int size);
    Reader getReader(long id);
    Reader addReader(Reader reader);
    Reader updateReader(long id, Reader reader);
    boolean deleteReader(long id);
}
