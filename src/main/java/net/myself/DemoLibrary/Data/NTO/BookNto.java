package net.myself.DemoLibrary.Data.NTO;
import net.myself.DemoLibrary.Data.Entities.Book;

import java.time.LocalDate;

public record BookNto(String title, AuthorNto author, String isbn, LocalDate publishedDate)
{
	public static BookNto fromBook(Book book)
	{
		return new BookNto(book.getTitle(), AuthorNto.fromAuthor(book.getAuthor()), book.getIsbn(), book.getPublishedDate());
	}
}
