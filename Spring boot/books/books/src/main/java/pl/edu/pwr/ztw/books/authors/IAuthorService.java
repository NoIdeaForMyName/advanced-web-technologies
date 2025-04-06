package pl.edu.pwr.ztw.books.authors;

import java.util.Collection;

public interface IAuthorService {
    Collection<Author> getAuthors();
    Author getAuthor(int id);
    Author addAuthor(Author author);
    Author updateAuthor(int id, Author author);
    boolean deleteAuthor(int id);
}