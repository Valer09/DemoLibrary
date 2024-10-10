package net.myself.DemoLibrary.Data.NTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import net.myself.DemoLibrary.Data.Entities.Book;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

public record BookNto(
				@Size(min = 1, max = 40)
				String title,
				@Size(min = 13, max = 13)
				String isbn,
				@Size(min = 2, max = 41)
				String author,
				@Size(min = 16, max = 16)
				String authorIsni,
				@PastOrPresent
				LocalDate publishedDate,
				@JsonIgnore AuthorNto authorNto)
{
	public static BookNto fromBook(Book book)
	{
		var author = book.getAuthor();
		return new BookNto(book.getTitle(),book.getIsbn(), author.getFullName(), author.getIsni(), book.getPublishedDate(), AuthorNto.fromAuthor(book.getAuthor()));
	}
}
