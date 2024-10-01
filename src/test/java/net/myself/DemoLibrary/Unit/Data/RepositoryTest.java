package net.myself.DemoLibrary.Unit.Data;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Infrastructure.Configuration.JacksonConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class RepositoryTest
{
	ObjectMapper jackson;
	@Autowired
	private IBookRepository repository;
	
	@BeforeAll
	public void setup()
	{
		jackson = new JacksonConfig().jackson();
	}
	
	@Test
	void containsTest() throws Exception
	{
		List<Book> list = new ArrayList<>(Arrays.asList(
						Book.createTransientBook( new BookNto("title", "test", "test", LocalDate.now())),
						Book.createTransientBook( new BookNto("titleOnStart", "test", "test1", LocalDate.now())),
						Book.createTransientBook( new BookNto("EndWithtitle", "test", "test2", LocalDate.now())),
						Book.createTransientBook( new BookNto("InTheMiddletitleIs", "test", "test3", LocalDate.now())),
						Book.createTransientBook( new BookNto("TitleCaps", "test", "test4", LocalDate.now())),
						Book.createTransientBook( new BookNto("TITLEisuppercase", "test", "test5", LocalDate.now())),
						Book.createTransientBook( new BookNto("test", "test", "test6", LocalDate.now()))));
		
		for(Book b : list) repository.save(b);
		
		var res = repository.findByTitleContainingIgnoreCase("title");
		System.out.println(jackson.writeValueAsString(res));
		Assertions.assertThat(res.size()).isEqualTo(6);
		for(int i = 0; i < list.size() - 1; i++)
		{
			Book b = list.get(i);
			assertEquals(b.getAuthor(), res.get(i).getAuthor());
			assertEquals(b.getTitle(), res.get(i).getTitle());
			assertEquals(b.getIsbn(), res.get(i).getIsbn());
			assertEquals(b.getPublishedDate(), res.get(i).getPublishedDate());
		}
	}
}
