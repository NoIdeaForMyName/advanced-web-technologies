package pl.edu.pwr.ztw.books.readers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Reader {

    private long id;
    private String name;
    private String lastName;
    private String email;

}
