package pl.edu.pwr.ztw.books.rentals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateRentalRequest {
    long readerId;
    long bookId;
}
