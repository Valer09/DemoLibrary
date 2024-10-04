package net.myself.DemoLibrary.Unit.Infrastructure.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@SpringBootTest
public class EncodingConsistencyTest
{
	@Autowired
	private ObjectMapper jackson;
	@Test
	void entityNtoTest()
	{
		var author = getStaticAuthor();
		BookNto bookNto = new BookNto("Françoise Sagan", "test", author.fullName(), author.isni(),  LocalDate.now(), author);
		
		Book bookEntity = Book.createTransientBook(bookNto);
		
		Assertions.assertThat(new String(bookNto.title().getBytes(StandardCharsets.UTF_8))).isEqualTo(bookEntity.getTitle());
		Assertions.assertThat(new String(bookNto.isbn().getBytes(StandardCharsets.UTF_8))).isEqualTo(bookEntity.getIsbn());
		Assertions.assertThat(new String(bookNto.author().getBytes(StandardCharsets.UTF_8))).isEqualTo(bookEntity.getAuthor().getFullName());
		
		Assertions.assertThat(bookNto.title()).isEqualTo(bookEntity.getTitle());
		Assertions.assertThat(bookNto.isbn()).isEqualTo(bookEntity.getIsbn());
		Assertions.assertThat(bookNto.author()).isEqualTo(bookEntity.getAuthor().getFullName());
	}
	
	@Test
	void jacksonTest() throws Exception
	{
		var author = getStaticAuthor();
		BookNto bookNto = new BookNto("Françoise Sagan", "test", author.fullName(), author.isni(),  LocalDate.now(), author);
		BookNto fromJson = jackson.readValue(jackson.writeValueAsString(bookNto), BookNto.class);
		
		Assertions.assertThat(bookNto.title()).isEqualTo(fromJson.title());
		Assertions.assertThat(bookNto.isbn()).isEqualTo(fromJson.isbn());
		Assertions.assertThat(bookNto.author()).isEqualTo(fromJson.author());
	}
	
	private AuthorNto getStaticAuthor()
	{
		return new AuthorNto("ABC123XYZ", "Françoise", "Sagan", "Françoise"+" "+"Sagan", LocalDate.now());
	}
}
