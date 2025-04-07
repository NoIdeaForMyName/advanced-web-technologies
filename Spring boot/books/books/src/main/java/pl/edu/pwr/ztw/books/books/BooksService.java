package pl.edu.pwr.ztw.books.books;

import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.activities.ActivityLog;
import pl.edu.pwr.ztw.books.authors.IAuthorService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class BooksService implements IBooksService {
    private final static List<Book> booksRepo = new ArrayList<>();
    private static long nextId = 1;

    //private final IAuthorService authorService;

    public BooksService(IAuthorService authorService) {
        //this.authorService = authorService;
        if (booksRepo.isEmpty()) {
            //booksRepo.add(new Book(nextId++, "Potop", authorService.getAuthor(1), 936));
            //booksRepo.add(new Book(nextId++, "Wesele", authorService.getAuthor(2), 150));
            //booksRepo.add(new Book(nextId++, "Dziady", authorService.getAuthor(3), 292));
            addBookWithLog(new Book(0, "Potop", authorService.getAuthor(1), 936), "System");
            addBookWithLog(new Book(0, "Wesele", authorService.getAuthor(2), 150), "System");
            addBookWithLog(new Book(0, "Dziady", authorService.getAuthor(3), 292), "System");
        }
    }

    @Override
    public Collection<Book> getBooks() {
        return booksRepo;
    }

    @Override
    public Book getBook(long id) {
        return booksRepo.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Book addBook(Book book) {
        if (book.getTitle() == null || book.getAuthor() == null || book.getPages() == null)
            return null;
        book = new Book(nextId++, book.getTitle(), book.getAuthor(), book.getPages());
        booksRepo.add(book);
        new ActivityLog("Added new book '" + book.getTitle() + "'");
        return book;
    }

    @Override
    public Book updateBook(long id, Book book) {
        Book existingBook = getBook(id);
        if (existingBook != null) {
            String oldTitle = existingBook.getTitle();
            existingBook.setTitle(book.getTitle() != null ? book.getTitle() : existingBook.getTitle());
            existingBook.setAuthor(book.getAuthor() != null ? book.getAuthor() : existingBook.getAuthor());
            existingBook.setPages(book.getPages() != null ? book.getPages() : existingBook.getPages());
            new ActivityLog("Updated book from '" + oldTitle + "' to '" + book.getTitle() + "'");
        }
        return existingBook;
    }

    @Override
    public boolean deleteBook(long id) {
        //return booksRepo.removeIf(b -> b.getId() == id);
        Book bookToRemove = getBook(id);
        if (bookToRemove != null && booksRepo.removeIf(b -> b.getId() == id)) {
            new ActivityLog("Deleted book '" + bookToRemove.getTitle() + "'");
            return true;
        }
        return false;
    }

    // Pomocnicza metoda do inicjalizacji
    private void addBookWithLog(Book book, String initiator) {
        Book newBook = new Book(nextId++, book.getTitle(), book.getAuthor(), book.getPages());
        booksRepo.add(newBook);
        new ActivityLog(initiator + " added initial book '" + newBook.getTitle() + "'");
    }
}