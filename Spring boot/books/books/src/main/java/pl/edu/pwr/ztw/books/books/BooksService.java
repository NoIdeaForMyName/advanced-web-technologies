package pl.edu.pwr.ztw.books.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.authors.IAuthorService;

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
            booksRepo.add(new Book(nextId++, "Potop", authorService.getAuthor(1), 936));
            booksRepo.add(new Book(nextId++, "Wesele", authorService.getAuthor(2), 150));
            booksRepo.add(new Book(nextId++, "Dziady", authorService.getAuthor(3), 292));
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
        return book;
    }

    @Override
    public Book updateBook(int id, Book book) {
        Book existingBook = getBook(id);
        if (existingBook != null) {
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setPages(book.getPages());
        }
        return existingBook;
    }

    @Override
    public boolean deleteBook(int id) {
        return booksRepo.removeIf(b -> b.getId() == id);
    }
}