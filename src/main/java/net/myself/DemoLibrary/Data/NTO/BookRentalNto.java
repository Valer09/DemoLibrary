package net.myself.DemoLibrary.Data.NTO;

import jakarta.validation.constraints.Size;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.Entities.BookRental;
import net.myself.DemoLibrary.Data.Entities.IBook;
import net.myself.DemoLibrary.Data.Entities.IBookRental;

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
	public static BookRentalNto createFrom(IBookRental saved)
	{
		return new BookRentalNto(saved.getRentalCode(), saved.getBookIsbn(), saved.getBookTitle(), saved.getAuthorName(), saved.getState(), saved.getStartingDate(), saved.getEndingDate());
	}
}
