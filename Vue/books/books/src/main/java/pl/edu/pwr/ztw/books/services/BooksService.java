package pl.edu.pwr.ztw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.models.ActivityLog;
import pl.edu.pwr.ztw.books.models.Book;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class BooksService implements IBooksService {
    private static List<Book> booksRepo = new ArrayList<>();
    private static int nextId = 1;

    @Autowired
    private IAuthorService authorService;

    static {
        // We'll initialize books in the constructor after authorService is autowired
    }

    @Autowired
    public void init() {
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
    public Book getBook(int id) {
        return booksRepo.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Book addBook(Book book) {
        book = new Book(nextId++, book.getTitle(), book.getAuthor(), book.getPages());
        booksRepo.add(book);
        new ActivityLog("Added new book '" + book.getTitle() + "'");
        return book;
    }

    @Override
    public Book updateBook(int id, Book book) {
        Book existingBook = getBook(id);
        if (existingBook != null) {
            String oldTitle = existingBook.getTitle();
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setPages(book.getPages());
            new ActivityLog("Updated book from '" + oldTitle + "' to '" + book.getTitle() + "'");
        }
        return existingBook;
    }

    @Override
    public boolean deleteBook(int id) {
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