package pl.edu.pwr.ztw.books.readers;

import java.util.Collection;

public interface IReadersService {
    Collection<Reader> getReaders();
    Reader getReader(long id);
    Reader addReader(Reader reader);
    Reader updateReader(long id, Reader reader);
    boolean deleteReader(long id);
}
