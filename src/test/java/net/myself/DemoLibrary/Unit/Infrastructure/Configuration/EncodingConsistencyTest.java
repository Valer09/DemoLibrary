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
		BookNto bookNto = new BookNto("Françoise Sagan", getStaticAuthor("ABC123XYZ"), "123-4567890123", LocalDate.now());
		
		Book bookEntity = Book.createTransientBook(bookNto);
		
		Assertions.assertThat(new String(bookNto.title().getBytes(StandardCharsets.UTF_8))).isEqualTo(bookEntity.getTitle());
		Assertions.assertThat(new String(bookNto.isbn().getBytes(StandardCharsets.UTF_8))).isEqualTo(bookEntity.getIsbn());
		Assertions.assertThat(new String(bookNto.author().name().getBytes(StandardCharsets.UTF_8))).isEqualTo(bookEntity.getAuthor().getName());
		
		Assertions.assertThat(bookNto.title()).isEqualTo(bookEntity.getTitle());
		Assertions.assertThat(bookNto.isbn()).isEqualTo(bookEntity.getIsbn());
		Assertions.assertThat(bookNto.author().name()).isEqualTo(bookEntity.getAuthor().getName());
	}
	
	@Test
	void jacksonTest() throws Exception
	{
		
		BookNto bookNto = new BookNto("Françoise Sagan", getStaticAuthor("ABC123XYZ"), "123-4567890123", LocalDate.now());
		var x = jackson.writeValueAsString(bookNto);
		BookNto fromJson = jackson.readValue(x, BookNto.class);
		
		Assertions.assertThat(bookNto.title()).isEqualTo(fromJson.title());
		Assertions.assertThat(bookNto.isbn()).isEqualTo(fromJson.isbn());
		Assertions.assertThat(bookNto.author().name()).isEqualTo(fromJson.author().name());
	}
	
	private AuthorNto getStaticAuthor(String cf)
	{
		return new AuthorNto(cf, "Françoise", "Sagan", LocalDate.now());
	}
}
