package net.myself.DemoLibrary.Data.NTO;

import jakarta.validation.constraints.Size;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.Entities.BookRental;

import java.time.LocalDate;

public record BookRentalNto(
														java.util.UUID rentalCode, @Size(min = 13, max = 13)
														String isbn,
                            String title,
                            String author,
                            String state,
                            LocalDate startDate,
                            LocalDate endDate)
{
	public static BookRentalNto createFrom(BookRental saved)
	{
		Book book = saved.getBook();
		return new BookRentalNto(saved.getRentalCode(), book.getIsbn(), book.getTitle(), book.getAuthor().getFullName(), saved.getState(), saved.getStartingDate(), saved.getEndingDate());
	}
}
