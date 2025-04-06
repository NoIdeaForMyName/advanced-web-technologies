package pl.edu.pwr.ztw.books.authors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class Author {

    private long id;
    private String name;
    private String lastName;

}
