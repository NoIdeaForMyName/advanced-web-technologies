package pl.edu.pwr.ztw.books.services;

import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.models.Author;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthorService implements IAuthorService {
    private static List<Author> authorsRepo = new ArrayList<>();
    private static int nextId = 1;

    static {
        authorsRepo.add(new Author(nextId++, "Henryk", "Sienkiewicz"));
        authorsRepo.add(new Author(nextId++, "Stanisław", "Reymont"));
        authorsRepo.add(new Author(nextId++, "Adam", "Mickiewicz"));
    }

    @Override
    public Collection<Author> getAuthors() {
        return authorsRepo;
    }

    @Override
    public Author getAuthor(int id) {
        return authorsRepo.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Author addAuthor(Author author) {
        author = new Author(nextId++, author.getName(), author.getLastName());
        authorsRepo.add(author);
        return author;
    }

    @Override
    public Author updateAuthor(int id, Author author) {
        Author existingAuthor = getAuthor(id);
        if (existingAuthor != null) {
            existingAuthor.setName(author.getName());
            existingAuthor.setLastName(author.getLastName());
        }
        return existingAuthor;
    }

    @Override
    public boolean deleteAuthor(int id) {
        return authorsRepo.removeIf(a -> a.getId() == id);
    }
}