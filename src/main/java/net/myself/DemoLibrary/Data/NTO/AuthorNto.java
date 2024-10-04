package net.myself.DemoLibrary.Data.NTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.myself.DemoLibrary.Data.Entities.Author;
import java.time.LocalDate;

public record AuthorNto(String isni, String name, String lastName, @JsonIgnore String fullName, LocalDate birth)
{
	public static AuthorNto fromAuthor(Author author)
	{
		return new AuthorNto(
						author.getIsni(),
						author.getName(),
						author.getLastname(),
						author.getFullName(),
						author.getBirth());
	}
}
