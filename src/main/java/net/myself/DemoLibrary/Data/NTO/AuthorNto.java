package net.myself.DemoLibrary.Data.NTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import net.myself.DemoLibrary.Data.Entities.Author;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

public record AuthorNto(
				@NotNull
				@Size(min = 16, max = 16)
				String isni,
				@NotNull
				@Size(min = 2, max = 20)
				String name,
				@NotNull
				@Size(min = 2, max = 20)
				String lastName,
				@JsonIgnore String fullName,
				@Past
				LocalDate birth)
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
