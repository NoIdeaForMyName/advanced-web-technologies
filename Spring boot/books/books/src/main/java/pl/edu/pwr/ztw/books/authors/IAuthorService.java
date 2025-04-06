package pl.edu.pwr.ztw.books.authors;

import java.util.Collection;

public interface IAuthorService {
    Collection<Author> getAuthors();
    Author getAuthor(long id);
    Author addAuthor(Author author);
    Author updateAuthor(long id, Author author);
    boolean deleteAuthor(long id);
}
