package net.myself.DemoLibrary.Unit.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.myself.DemoLibrary.Data.Entities.Author;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.Repository.IAuthorRepository;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Infrastructure.Configuration.JacksonConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
	private IBookRepository bookRepository;
	@Autowired
	private IAuthorRepository authorRepository;
	@BeforeAll
	public void setup()
	{
		jackson = new JacksonConfig().jackson();
	}
	
	@Test
	void containsTest() throws Exception
	{
		List<Author> authors = new ArrayList<>();
		for (int i = 0; i < 7; i++)
		{
			authors.add(new Author("Author " + (char) ('a' + i), "test", "test", LocalDate.now(), new ArrayList<>()));
			authorRepository.save(authors.get(i));
		}
		
		List<Book> list = new ArrayList<>(Arrays.asList(
						new Book(0, "title", authors.get(0), "test", LocalDate.now()),
						new Book(0,"titleOnStart", authors.get(1), "test1", LocalDate.now()),
						new Book(0,"EndWithtitle", authors.get(2), "test2", LocalDate.now()),
						new Book(0,"InTheMiddletitleIs", authors.get(3), "test3", LocalDate.now()),
						new Book(0,"TitleCaps", authors.get(4), "test4", LocalDate.now()),
						new Book(0,"TITLEisuppercase", authors.get(5), "test5", LocalDate.now()),
						new Book(0,"test", authors.get(6), "test6" , LocalDate.now())));
		
		for(Book b : list) bookRepository.save(b);
		
		var res = bookRepository.findByTitleContainingIgnoreCaseAndDeletedFalse("title");
		
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
