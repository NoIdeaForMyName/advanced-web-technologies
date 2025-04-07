package pl.edu.pwr.ztw.books.authors;

import pl.edu.pwr.ztw.books.PaginatedResponse;

import java.util.Collection;

public interface IAuthorService {
    Collection<Author> getAuthors();
    PaginatedResponse<Author> getPaginatedAuthors(int page, int size);
    Author getAuthor(long id);
    Author addAuthor(Author author);
    Author updateAuthor(long id, Author author);
    boolean deleteAuthor(long id);
}
