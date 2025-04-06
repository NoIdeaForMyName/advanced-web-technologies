package pl.edu.pwr.ztw.books.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class Author {

    private int id;
    private String name;
    private String lastName;

}
