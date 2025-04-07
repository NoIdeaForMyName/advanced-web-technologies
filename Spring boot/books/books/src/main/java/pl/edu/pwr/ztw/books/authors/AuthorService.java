package pl.edu.pwr.ztw.books.authors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.PaginatedResponse;
import pl.edu.pwr.ztw.books.activities.ActivityLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthorService implements IAuthorService {
    private static final List<Author> authorsRepo = new ArrayList<>();
    private static long nextId = 1;

    /*
    public AuthorService() {
        authorsRepo.add(new Author(nextId++, "Henryk", "Sienkiewicz"));
        authorsRepo.add(new Author(nextId++, "Stanisław", "Reymont"));
        authorsRepo.add(new Author(nextId++, "Adam", "Mickiewicz"));
    }
    */

    @Autowired
    public void init() {
        if (authorsRepo.isEmpty()) {
            addAuthorWithLog(new Author(0, "Henryk", "Sienkiewicz"), "System");
            addAuthorWithLog(new Author(0, "Stanisław", "Reymont"), "System");
            addAuthorWithLog(new Author(0, "Adam", "Mickiewicz"), "System");
        }
    }

    @Override
    public PaginatedResponse<Author> getPaginatedAuthors(int page, int perPage) {
        int totalItems = authorsRepo.size();
        int totalPages = (int) Math.ceil((double) totalItems / perPage);

        // Zabezpieczenie przed nieprawidłowymi wartościami page
        page = Math.max(1, Math.min(page, totalPages));

        // Spring Data i większość systemów paginacji używa indeksowania od 0,
        // ale Twój frontend oczekuje numeracji stron od 1
        int fromIndex = (page - 1) * perPage;
        int toIndex = Math.min(fromIndex + perPage, totalItems);

        List<Author> pageContent = authorsRepo.subList(fromIndex, toIndex);

        return new PaginatedResponse<>(
                pageContent,
                page,
                perPage,
                totalPages,
                totalItems
        );
    }

    @Override
    public Collection<Author> getAuthors() {
        return authorsRepo;
    }

    @Override
    public Author getAuthor(long id) {
        return authorsRepo.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Author addAuthor(Author author) {
        if (author.getName() == null || author.getLastName() == null)
            return null;
        author = new Author(nextId++, author.getName(), author.getLastName());
        authorsRepo.add(author);
        new ActivityLog("Added new author '" + author.getName() + " " + author.getLastName() + "'");
        return author;
    }

    @Override
    public Author updateAuthor(long id, Author author) {
        Author existingAuthor = getAuthor(id);
        if (existingAuthor != null) {
            existingAuthor.setName(author.getName() != null ? author.getName() : existingAuthor.getName());
            existingAuthor.setLastName(author.getLastName() != null ? author.getLastName() : existingAuthor.getLastName());
            String oldName = existingAuthor.getName() + " " + existingAuthor.getLastName();
            new ActivityLog("Updated author from '" + oldName + "' to '" + author.getName() + " " + author.getLastName() + "'");
        }
        return existingAuthor;
    }

    @Override
    public boolean deleteAuthor(long id) {
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

    // Pomocnicza metoda do inicjalizacji
    private void addAuthorWithLog(Author author, String initiator) {
        Author newAuthor = new Author(nextId++, author.getName(), author.getLastName());
        authorsRepo.add(newAuthor);
        new ActivityLog(initiator + " added initial author '" + newAuthor.getName() + " " + newAuthor.getLastName() + "'");
    }
}