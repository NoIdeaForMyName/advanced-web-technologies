package pl.edu.pwr.ztw.books.rentals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UpdateRentalRequest {
    long readerId;
    long bookId;
    LocalDateTime rentalDate;
    LocalDateTime returnDate;
}
