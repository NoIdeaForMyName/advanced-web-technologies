package pl.edu.pwr.ztw.books.readers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.ztw.books.PaginatedResponse;
import pl.edu.pwr.ztw.books.activities.ActivityLog;
import pl.edu.pwr.ztw.books.authors.Author;
import pl.edu.pwr.ztw.books.books.Book;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ReadersService implements IReadersService {

    private static final List<Reader> readersRepo = new ArrayList<>();
    private static long nextId = 1;

    public ReadersService() {
        //readersRepo.add(new Reader(nextId++, "Adam", "Abak", "adam.abak@example.com"));
        //readersRepo.add(new Reader(nextId++, "Mateusz", "Kamiński", "mateusz.kaminski@example.com"));
        //readersRepo.add(new Reader(nextId++, "Olaf", "Nowak", "olaf.nowak@example.com"));
    }

    @Autowired
    public void init() {
        if (readersRepo.isEmpty()) {
            addReaderWithLog(new Reader(0, "Adam", "Abak", "adam.abak@example.com"), "System");
            addReaderWithLog(new Reader(0, "Mateusz", "Kamiński", "mateusz.kaminski@example.com"), "System");
            addReaderWithLog(new Reader(0, "Olaf", "Nowak", "olaf.nowak@example.com"), "System");
        }
    }

    @Override
    public PaginatedResponse<Reader> getPaginatedReaders(int page, int perPage) {
        int totalItems = readersRepo.size();
        int totalPages = (int) Math.ceil((double) totalItems / perPage);

        // Zabezpieczenie przed nieprawidłowymi wartościami page
        page = Math.max(1, Math.min(page, totalPages));

        // Spring Data i większość systemów paginacji używa indeksowania od 0,
        // ale Twój frontend oczekuje numeracji stron od 1
        int fromIndex = (page - 1) * perPage;
        int toIndex = Math.min(fromIndex + perPage, totalItems);

        List<Reader> pageContent = readersRepo.subList(fromIndex, toIndex);

        return new PaginatedResponse<>(
                pageContent,
                page,
                perPage,
                totalPages,
                totalItems
        );
    }

    @Override
    public Collection<Reader> getReaders() {
        return readersRepo;
    }

    @Override
    public Reader getReader(long id) {
        return readersRepo.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Reader addReader(Reader reader) {
        if (reader.getName() == null || reader.getLastName() == null || reader.getEmail() == null)
            return null;
        reader = new Reader(nextId++, reader.getName(), reader.getLastName(), reader.getEmail());
        readersRepo.add(reader);
        new ActivityLog("Added new reader '" + reader.getName() + reader.getLastName() + "'");
        return reader;
    }

    @Override
    public Reader updateReader(long id, Reader reader) {
        Reader existingReader = getReader(id);
        if (existingReader != null) {
            String oldReder = existingReader.getName() + " " + existingReader.getLastName();
            existingReader.setName(reader.getName() != null ? reader.getName() : existingReader.getName());
            existingReader.setLastName(reader.getLastName() != null ? reader.getLastName() : existingReader.getLastName());
            new ActivityLog("Updated reader '" + oldReder + "' to '" + reader.getName() + reader.getLastName() + "'");
        }
        return existingReader;
    }

    @Override
    public boolean deleteReader(long id) {
        //return readersRepo.removeIf(r -> r.getId() == id);
        Reader readerToRemove = getReader(id);
        if (readerToRemove != null && readersRepo.removeIf(r -> r.getId() == id)) {
            new ActivityLog("Deleted reader '" + readerToRemove.getName() + " " + readerToRemove.getLastName() + "'");
            return true;
        }
        return false;
    }

    // Pomocnicza metoda do inicjalizacji
    private void addReaderWithLog(Reader reader, String initiator) {
        Reader newReader = new Reader(nextId++, reader.getName(), reader.getLastName(), reader.getEmail());
        readersRepo.add(newReader);
        new ActivityLog(initiator + " added initial reader '" + newReader.getName() + " " + newReader.getLastName() + "'");
    }
}
