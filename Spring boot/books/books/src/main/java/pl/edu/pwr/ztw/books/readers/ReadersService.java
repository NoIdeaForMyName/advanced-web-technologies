package pl.edu.pwr.ztw.books.readers;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ReadersService implements IReadersService {

    private static final List<Reader> readersRepo = new ArrayList<>();
    private static long nextId = 1;

    public ReadersService() {
        readersRepo.add(new Reader(nextId++, "Adam", "Abak", "adam.abak@example.com"));
        readersRepo.add(new Reader(nextId++, "Mateusz", "Kamiński", "mateusz.kaminski@example.com"));
        readersRepo.add(new Reader(nextId++, "Olaf", "Nowak", "olaf.nowak@example.com"));
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
        return reader;
    }

    @Override
    public Reader updateReader(long id, Reader reader) {
        Reader existingReader = getReader(id);
        if (existingReader != null) {
            existingReader.setName(reader.getName() != null ? reader.getName() : existingReader.getName());
            existingReader.setLastName(reader.getLastName() != null ? reader.getLastName() : existingReader.getLastName());
        }
        return existingReader;
    }

    @Override
    public boolean deleteReader(long id) {
        return readersRepo.removeIf(r -> r.getId() == id);
    }
}
