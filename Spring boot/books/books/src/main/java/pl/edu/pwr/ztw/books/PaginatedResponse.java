package pl.edu.pwr.ztw.books;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedResponse<T> {
    private List<T> data; // Zmieniamy z 'content' na 'data' jeśli frontend tego oczekuje
    private int current_page;
    private int per_page;
    private int total_pages;
    private long total_items;

    public PaginatedResponse(List<T> data, int current_page, int per_page,
                             int total_pages, long total_items) {
        this.data = data;
        this.current_page = current_page;
        this.per_page = per_page;
        this.total_pages = total_pages;
        this.total_items = total_items;
    }

    // Gettery i settery
    // Upewnij się, że nazwy pól w JSON będą zgodne z konwencją snake_case
    // lub użyj @JsonProperty jeśli potrzebujesz innej konwencji
}