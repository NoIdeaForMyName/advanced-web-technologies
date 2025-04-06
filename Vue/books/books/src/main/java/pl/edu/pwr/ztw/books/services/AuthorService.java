package pl.edu.pwr.ztw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.models.ActivityLog;
import pl.edu.pwr.ztw.books.models.Author;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthorService implements IAuthorService {
    private static List<Author> authorsRepo = new ArrayList<>();
    private static int nextId = 1;

    static {
        //authorsRepo.add(new Author(nextId++, "Henryk", "Sienkiewicz"));
        //.add(new Author(nextId++, "Stanisław", "Reymont"));
        //authorsRepo.add(new Author(nextId++, "Adam", "Mickiewicz"));
    }

    @Autowired
    public void init() {
        if (authorsRepo.isEmpty()) {
            addAuthorWithLog(new Author(0, "Henryk", "Sienkiewicz"), "System");
            addAuthorWithLog(new Author(0, "Stanisław", "Reymont"), "System");
            addAuthorWithLog(new Author(0, "Adam", "Mickiewicz"), "System");
        }
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
        new ActivityLog("Added new author '" + author.getName() + " " + author.getLastName() + "'");
        return author;
    }

    @Override
    public Author updateAuthor(int id, Author author) {
        Author existingAuthor = getAuthor(id);
        if (existingAuthor != null) {
            String oldName = existingAuthor.getName() + " " + existingAuthor.getLastName();
            existingAuthor.setName(author.getName());
            existingAuthor.setLastName(author.getLastName());
            new ActivityLog("Updated author from '" + oldName + "' to '" + author.getName() + " " + author.getLastName() + "'");
        }
        return existingAuthor;
    }

    @Override
    public boolean deleteAuthor(int id) {
        //return authorsRepo.removeIf(a -> a.getId() == id);
        return authorsRepo.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .map(author -> {
                    authorsRepo.remove(author);
                    new ActivityLog("Deleted author '" + author.getName() + " " + author.getLastName() + "'");
                    return true;
                })
                .orElse(false);
    }

    private void addAuthorWithLog(Author author, String initiator) {
        Author newAuthor = new Author(nextId++, author.getName(), author.getLastName());
        authorsRepo.add(newAuthor);
        new ActivityLog(initiator + " added initial author '" + newAuthor.getName() + " " + newAuthor.getLastName() + "'");
    }
}