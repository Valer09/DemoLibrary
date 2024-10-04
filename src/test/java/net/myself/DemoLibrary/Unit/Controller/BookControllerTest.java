package net.myself.DemoLibrary.Unit.Controller;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.myself.DemoLibrary.Controller.BookController;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Helper.BookControllerEndPointsMap;
import net.myself.DemoLibrary.Helper.BookHelper;
import net.myself.DemoLibrary.Service.BookService;
import net.myself.DemoLibrary.Service.ServiceResponse;
import net.myself.DemoLibrary.Service.ServiceResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
class BookControllerTest
{
	@Autowired
	private ObjectMapper jackson;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private BookService bookService;
	
	@Test
	void getAllBooks() throws Exception
	{
		List<Book> bookList = new ArrayList<>(Arrays.asList(
						BookHelper.getRandomBook(),
						BookHelper.getRandomBook(),
						BookHelper.	getRandomBook(),
						BookHelper.	getRandomBook()));
		
		when(bookService.getAllBooksNto()).thenReturn(bookList.stream().map(BookNto::fromBook).collect(Collectors.toList()));
		
		MvcResult mvcResult = mockMvc.perform(BookControllerEndPointsMap.getAllBooks())
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)))
						.andReturn();
		
		// Serialize the response and using Assertions.[...]. For an example of direct access and assertions on json response fields values, look findByIsbn test
		List<BookNto> response = jackson.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
		for(int i = 0; i < response.size(); i++)
		{
			BookNto b = response.get(i);
			Assertions.assertThat(b.isbn()).isEqualTo(bookList.get(i).getIsbn());
			Assertions.assertThat(b.title()).isEqualTo(bookList.get(i).getTitle());
			Assertions.assertThat(b.authorIsni()).isEqualTo(bookList.get(i).getAuthor().getIsni());
		}
		
		verify(bookService, times(1)).getAllBooksNto();
	}
	
	@Test
	void findByIsbn() throws Exception
	{
		String isbn = "abcd";
		AuthorNto aut = getAuthor();
		when(bookService.findByIsbnNto(isbn)).thenReturn(Optional.of(new BookNto("test",isbn,aut.name(), aut.isni(), LocalDate.now(), aut)));
		
		mockMvc.perform(BookControllerEndPointsMap.findByIbsn(isbn))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(isbn));
		
		verify(bookService, times(1)).findByIsbnNto(isbn);
	}
	
	@Test
	void findByTitle() throws Exception
	{
		String title = "title";
		AuthorNto aut = getAuthor();
		AuthorNto aut2 = getAuthor();
		List<BookNto> books = new ArrayList<>(Arrays.asList
						(
										new BookNto(title, aut.isni(), aut.name(), aut.isni(), LocalDate.now(), aut),
										new BookNto(title, aut2.isni(), aut2.name(), aut2.isni(), LocalDate.now(), aut2)
						));
		
		when(bookService.findByTitleNto(title)).thenReturn(books);
		
		mockMvc.perform(BookControllerEndPointsMap.findByTitle(title))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(title))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(title));
		
		verify(bookService, times(1)).findByTitleNto(title);
	}
	
	@Test
	void searchByTitle() throws Exception
	{
		String title = "Title";
		AuthorNto aut = getAuthor();
		AuthorNto aut2 = getAuthor();
		List<BookNto> books = new ArrayList<>(Arrays.asList
						(
										new BookNto(title, aut.isni(), aut.name(), aut.isni(), LocalDate.now(), aut),
										new BookNto(title, aut2.isni(), aut2.name(), aut2.isni(), LocalDate.now(), aut2),
										new BookNto("contains"+title, aut2.isni(), aut2.name(), aut2.isni(), LocalDate.now(), aut2))
						);
		
		when(bookService.findByTitleContainingIgnoreCaseNto(title)).thenReturn(books);
		
		mockMvc.perform(BookControllerEndPointsMap.searchByTitle(title))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(title))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(title))
						.andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("containsTitle"));
		
		verify(bookService, times(1)).findByTitleContainingIgnoreCaseNto(title);
	}
	
	@Test
	void addBook() throws Exception
	{
		BookNto book = BookNto.fromBook(BookHelper.getRandomBook());
		var serialized = jackson.writeValueAsString(book);
		var deserialized = jackson.readValue(serialized, BookNto.class);
		
		when(bookService.addBookFromNto(deserialized)).thenReturn(ServiceResponse.createOk(book));
		
		MvcResult mvcResult = mockMvc.perform(BookControllerEndPointsMap.addBook(serialized))
						.andDo(print())
						.andExpect(status().isCreated())
						.andReturn();
		
		BookNto addedBook = jackson.readValue(mvcResult.getResponse().getContentAsString(), BookNto.class);
		
		Assertions.assertThat(addedBook.title()).isEqualTo(book.title());
		Assertions.assertThat(addedBook.author()).isEqualTo(book.author());
		Assertions.assertThat(addedBook.isbn()).isEqualTo(book.isbn());
		Assertions.assertThat(addedBook.publishedDate()).isEqualTo(book.publishedDate());
		
		verify(bookService, times(1)).addBookFromNto(deserialized);
	}
	
	@Test
	void deleteBookByIsbn() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		
		when(bookService.deleteBookByIsbn(book.getIsbn())).thenReturn(ServiceResponse.createOk(""));
		
		performDeleteByIsbn(book).andExpect(status().isOk());
		
		verify(bookService, times(1)).deleteBookByIsbn(book.getIsbn());
	}
	
	@Test
	void deleteBookByIsbnNotFound() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		
		when(bookService.deleteBookByIsbn(book.getIsbn())).thenReturn(ServiceResponse.createError(ServiceResult.NOT_FOUND, ""));
		
		performDeleteByIsbn(book).andExpect(status().isNotFound());
		
		verify(bookService, times(1)).deleteBookByIsbn(book.getIsbn());
	}
	
	@Test
	void updateBook() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		AuthorNto author = getAuthor();
		BookNto newBook = new BookNto(book.getTitle()+"ed", book.getIsbn(), author.fullName(), author.isni(), LocalDate.now().minus(3, ChronoUnit.YEARS), author);
		
		BookUpdateNto nto = new BookUpdateNto(book.getIsbn(), newBook.title(), newBook.authorIsni(), newBook.publishedDate());
		
		when(bookService.updateBookFromNto(nto)).thenReturn(ServiceResponse.createOk(newBook));
		
		MvcResult mvcResult = performUpdate(nto).andExpect(status().isOk()).andReturn();
		
		BookNto updatedBook = jackson.readValue(mvcResult.getResponse().getContentAsString(), BookNto.class);
		
		Assertions.assertThat(updatedBook.title()).isEqualTo(newBook.title());
		Assertions.assertThat(updatedBook.authorIsni()).isEqualTo(newBook.authorIsni());
		Assertions.assertThat(updatedBook.isbn()).isEqualTo(book.getIsbn());
		Assertions.assertThat(updatedBook.publishedDate()).isEqualTo(newBook.publishedDate());
		
		verify(bookService, times(1)).updateBookFromNto(nto);
	}
	
	@Test
	void updateBookNotFound() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		AuthorNto author = getAuthor();
		BookNto newBook = new BookNto(book.getTitle()+"ed", book.getIsbn(), author.fullName(), author.isni(), LocalDate.now().minus(3, ChronoUnit.YEARS), author);
		
		BookUpdateNto nto = new BookUpdateNto(book.getIsbn(), newBook.title(), newBook.authorIsni(), newBook.publishedDate());
		
		when(bookService.updateBookFromNto(nto)).thenReturn(ServiceResponse.createError(ServiceResult.NOT_FOUND, ""));
		
		performUpdate(nto).andExpect(status().isNotFound());
		
		verify(bookService, times(1)).updateBookFromNto(nto);
	}
	
	private static AuthorNto getAuthor()
	{
		return new AuthorNto("A", "test", "test", "test test", LocalDate.now());
	}
	
	private ResultActions performDeleteByIsbn(Book book) throws Exception
	{
		return mockMvc.perform(BookControllerEndPointsMap.deleteBookByIsbn(book)).andDo(print());
	}
	
	private ResultActions performUpdate(BookUpdateNto nto) throws Exception
	{
		return mockMvc.perform(BookControllerEndPointsMap.updateBook(jackson.writeValueAsString(nto))).andDo(print());
	}
}