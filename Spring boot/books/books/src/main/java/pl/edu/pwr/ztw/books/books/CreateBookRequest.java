package pl.edu.pwr.ztw.books.books;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateBookRequest {
    String title;
    Long authorId;
    Integer pages;
}