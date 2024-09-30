package net.myself.DemoLibrary.Unit.Controller;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.myself.DemoLibrary.Controller.BookController;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Unit.Controller.Helper.BookControllerRequestMap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO: Aggiungere i verify. Refactoring per duplicazione

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
class BookControllerTest
{
	@Autowired
	private ObjectMapper jackson;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private IBookRepository bookRepository;
	
	@Test
	void checkJacksonConfiguration()
	{
		assertFalse(jackson.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
	}
	
	@Test
	void getAllBooks() throws Exception
	{
		Long bookId = 1L;
		List<Book> bookList = new ArrayList<>(Arrays.asList(
						new Book(bookId, "Test Book", "Author",  "123455", LocalDate.now()),
						new Book(bookId+1, "Test Book1", "Author1",  "123456", LocalDate.now()),
						new Book(bookId+2, "Test Book2", "Author2",  "123457", LocalDate.now()),
						new Book(bookId+3, "Test Book3", "Author3",  "123458", LocalDate.now())));
		
		when(bookRepository.findAll()).thenReturn(bookList);
		
		MvcResult mvcResult = mockMvc.perform(BookControllerRequestMap.getAllBooks())
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)))
						.andReturn();
		
		List<Book> response = jackson.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
		for(int i = 0; i < response.size(); i++)
		{
			Book b = response.get(i);
			Assertions.assertThat(b.getId()).isEqualTo(bookId+i);
			Assertions.assertThat(b.getIsbn()).isEqualTo(bookList.get(i).getIsbn());
			Assertions.assertThat(b.getTitle()).isEqualTo(bookList.get(i).getTitle());
			Assertions.assertThat(b.getAuthor()).isEqualTo(bookList.get(i).getAuthor());
		}
	}
	
	@Test
	void findByIsbn() throws Exception
	{
		when(bookRepository.findByIsbn("abcd")).thenReturn(Optional.of(new Book(1,"test","test","abcd",LocalDate.now())));
		mockMvc.perform(BookControllerRequestMap.findByIbsn())
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("abcd"));
	}
	
	@Test
	void findByTitle() throws Exception
	{
		List<Book> books = new ArrayList<>(Arrays.asList
						(
										new Book(1,"title","author","abcd",LocalDate.now()),
										new Book(1,"title","author","zxy",LocalDate.now())
						));
		
		when(bookRepository.findByTitle("title")).thenReturn(books);
		
		mockMvc.perform(BookControllerRequestMap.findByTitle())
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("title"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("title"));
	}
	
	@Test
	void searchByTitle() throws Exception
	{
		List<Book> books = new ArrayList<>(Arrays.asList
						(
										new Book(1,"title","author","abcd",LocalDate.now()),
										new Book(1,"title","author","zxy",LocalDate.now()),
										new Book(1,"containsTitle","author","zxy",LocalDate.now())
						));
		
		when(bookRepository.findByTitle("title")).thenReturn(books);
		
		mockMvc.perform(BookControllerRequestMap.findByTitle())
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("title"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("title"))
						.andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("containsTitle"));
	}
	
	@Test
	void addBook() throws Exception
	{
		
		LocalDate now = LocalDate.now();
		Book book = new Book(1, "title", "author", "isbn-0000", now);
		
		String jsonBook = jackson.writeValueAsString(book);
		System.out.println(jsonBook);
		System.out.println(jackson.writeValueAsString(now));
		when(bookRepository.save(book)).thenReturn(book);
		
		MvcResult mvcResult = mockMvc.perform(BookControllerRequestMap.addBook(jsonBook))
						.andDo(print())
						.andExpect(status().isCreated())
						.andReturn();
		
		Book addedBook = jackson.readValue(mvcResult.getResponse().getContentAsString(), Book.class);
		
		Assertions.assertThat(addedBook.getTitle()).isEqualTo(book.getTitle());
		Assertions.assertThat(addedBook.getAuthor()).isEqualTo(book.getAuthor());
		Assertions.assertThat(addedBook.getIsbn()).isEqualTo(book.getIsbn());
		Assertions.assertThat(addedBook.getPublishedDate()).isEqualTo(book.getPublishedDate());
	}
	
	@Test
	void deleteBookByIsbn() throws Exception
	{
		LocalDate now = LocalDate.now();
		Book book = new Book(1, "title", "author", "isbn-0000", now);
		
		when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(true);
		
		mockMvc.perform(BookControllerRequestMap.deleteBookByIsbn(book))
						.andDo(print())
						.andExpect(status().isOk());
		
		verify(bookRepository).deleteByIsbn(book.getIsbn());
	}
	
	@Test
	void deleteBookByIsbnNotFound() throws Exception
	{
		LocalDate now = LocalDate.now();
		Book book = new Book(1, "title", "author", "isbn-0000", now);
		
		when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(false);
		
		mockMvc.perform(BookControllerRequestMap.deleteBookByIsbn(book))
						.andDo(print())
						.andExpect(status().isNotFound());
		verify(bookRepository, never()).deleteByIsbn(book.getIsbn());
	}
	
	@Test
	void deleteBookById() throws Exception
	{
		LocalDate now = LocalDate.now();
		Book book = new Book(1, "title", "author", "isbn-0000", now);
		
		when(bookRepository.existsById(book.getId())).thenReturn(true);
		
		mockMvc.perform(BookControllerRequestMap.deleteBookById(book))
						.andDo(print())
						.andExpect(status().isOk());
		verify(bookRepository).deleteById(book.getId());
	}
	
	@Test
	void deleteBookByIdNotFound() throws Exception
	{
		LocalDate now = LocalDate.now();
		Book book = new Book(1, "title", "author", "isbn-0000", now);
		
		when(bookRepository.existsById(book.getId())).thenReturn(false);
		
		mockMvc.perform(BookControllerRequestMap.deleteBookById(book))
						.andDo(print())
						.andExpect(status().isNotFound());
		verify(bookRepository, never()).deleteById(book.getId());
	}
	
	@Test
	void deleteBook() throws Exception
	{
		LocalDate now = LocalDate.now();
		Book book = new Book(1, "title", "author", "isbn-0000", now);
		
		String jsonBook = jackson.writeValueAsString(book);
		
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book)));
		
		mockMvc.perform(BookControllerRequestMap.deleteBook(jsonBook))
						.andDo(print())
						.andExpect(status().isOk());
		verify(bookRepository).delete(book);
	}
	
	@Test
	void deleteBookConflict() throws Exception
	{
		LocalDate now = LocalDate.now();
		Book book = new Book(1, "title", "author", "isbn-0000", now);
		
		String jsonBook = jackson.writeValueAsString(book);
		
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book, book)));
		
		mockMvc.perform(BookControllerRequestMap.deleteBook(jsonBook))
						.andDo(print())
						.andExpect(status().isConflict());
		verify(bookRepository, never()).delete(book);
	}
	
	@Test
	void deleteBookNotFound() throws Exception
	{
		LocalDate now = LocalDate.now();
		Book book = new Book(1, "title", "author", "isbn-0000", now);
		
		String jsonBook = jackson.writeValueAsString(book);
		
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>());
		
		mockMvc.perform(BookControllerRequestMap.deleteBook(jsonBook))
						.andDo(print())
						.andExpect(status().isNotFound());
		verify(bookRepository, never()).delete(book);
	}
	
	@Test
	void updateBook() throws Exception
	{
		LocalDate now = LocalDate.now();
		Book book = new Book(1, "title", "author", "isbn-0000", now);
		Book newBook = new Book(1, "newtitle", "newauthor", "isbn-0000new", now);
		BookUpdateNto nto = new BookUpdateNto(book, newBook);
		
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book)));
		
		//Directly reading json
		/*mockMvc.perform(BookControllerRequestMap.updateBook(jackson.writeValueAsString(nto)))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$.title").value(newBook.getTitle()))
						.andExpect(MockMvcResultMatchers.jsonPath("$.author").value(newBook.getAuthor()))
						.andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(newBook.getIsbn()))
						.andExpect(MockMvcResultMatchers.jsonPath("$.publishedDate[0]").value(newBook.getPublishedDate().getYear()))
						.andExpect(MockMvcResultMatchers.jsonPath("$.publishedDate[1]").value(newBook.getPublishedDate().getMonthValue()))
						.andExpect(MockMvcResultMatchers.jsonPath("$.publishedDate[2]").value(newBook.getPublishedDate().getDayOfMonth()));*/
		
		//Serialize the response and using Assertions.[...]
		MvcResult mvcResult = mockMvc.perform(BookControllerRequestMap.updateBook(jackson.writeValueAsString(nto)))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();
		
		Book updatedBook = jackson.readValue(mvcResult.getResponse().getContentAsString(), Book.class);
		
		Assertions.assertThat(updatedBook.getTitle()).isEqualTo(newBook.getTitle());
		Assertions.assertThat(updatedBook.getAuthor()).isEqualTo(newBook.getAuthor());
		Assertions.assertThat(updatedBook.getIsbn()).isEqualTo(newBook.getIsbn());
		Assertions.assertThat(updatedBook.getPublishedDate()).isEqualTo(newBook.getPublishedDate());
		
		verify(bookRepository).save(book);
	}
	
	@Test
	void updateBookConflict() throws Exception
	{
		LocalDate now = LocalDate.now();
		Book book = new Book(1, "title", "author", "isbn-0000", now);
		Book newBook = new Book(1, "newtitle", "newauthor", "isbn-0000new", now);
		BookUpdateNto nto = new BookUpdateNto(book, newBook);
		
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book, book)));
		
		mockMvc.perform(BookControllerRequestMap.updateBook(jackson.writeValueAsString(nto)))
						.andDo(print())
						.andExpect(status().isConflict());
		
		verify(bookRepository, never()).save(book);
	}
	
	@Test
	void updateBookNotFound() throws Exception
	{
		LocalDate now = LocalDate.now();
		Book book = new Book(1, "title", "author", "isbn-0000", now);
		Book newBook = new Book(1, "newtitle", "newauthor", "isbn-0000new", now);
		BookUpdateNto nto = new BookUpdateNto(book, newBook);
		
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of()));
		
		mockMvc.perform(BookControllerRequestMap.updateBook(jackson.writeValueAsString(nto)))
						.andDo(print())
						.andExpect(status().isNotFound());
		
		verify(bookRepository, never()).save(book);
	}
	
}