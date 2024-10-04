package net.myself.DemoLibrary.Data.NTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.myself.DemoLibrary.Data.Entities.Book;

import java.time.LocalDate;

public record BookNto(
				String title,
				String isbn,
				String author,
				String authorIsni,
				LocalDate publishedDate,
				@JsonIgnore AuthorNto authorNto)
{
	public static BookNto fromBook(Book book)
	{
		var author = book.getAuthor();
		return new BookNto(book.getTitle(),book.getIsbn(), author.getFullName(), author.getIsni(), book.getPublishedDate(), AuthorNto.fromAuthor(book.getAuthor()));
	}
}
