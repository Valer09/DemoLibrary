package net.myself.DemoLibrary.Data.NTO;
import net.myself.DemoLibrary.Data.Entities.Author;
import java.time.LocalDate;

public record AuthorNto(String cf, String name, String surname, LocalDate birth)
{
	public static AuthorNto fromAuthor(Author author)
	{
		return new AuthorNto(
						author.getCf(),
						author.getName(),
						author.getSurname(),
						author.getBirth());
	}
}
