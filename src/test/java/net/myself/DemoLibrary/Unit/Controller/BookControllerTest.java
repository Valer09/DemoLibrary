package net.myself.DemoLibrary.Unit.Controller;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.myself.DemoLibrary.ControllerTestConfig;
import net.myself.DemoLibrary.Controller.BookController;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Helper.BookControllerEndPointsMap;
import net.myself.DemoLibrary.Helper.BookController_ServiceACT;
import net.myself.DemoLibrary.Helper.BookHelper;
import net.myself.DemoLibrary.Helper.HttpTestCase;
import net.myself.DemoLibrary.Service.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
@Import(ControllerTestConfig.class)
public class BookControllerTest
{
	@Autowired
	BookControllerEndPointsMap bookControllerEndPointsMap;
	@Autowired
	private ObjectMapper jackson;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private BookService bookService;
	
	@Test
	@DisplayName("Test get all books endpoint")
	void getAllBooks() throws Exception
	{
		List<Book> bookList = new ArrayList<>(Arrays.asList(
						BookHelper.getRandomBook(),
						BookHelper.getRandomBook(),
						BookHelper.	getRandomBook(),
						BookHelper.	getRandomBook()));
		
		when(bookService.getAllBooksNto()).thenReturn(bookList.stream().map(BookNto::fromBook).collect(Collectors.toList()));
		
		MvcResult mvcResult = mockMvc.perform(bookControllerEndPointsMap.getAllBooks())
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
	@DisplayName("Test findByIsbn endpoint")
	void findByIsbn() throws Exception
	{
		String isbn = "aaaaaaaaaaaaa";
		AuthorNto aut = getAuthor();
		when(bookService.findByIsbnNto(isbn)).thenReturn(Optional.of(new BookNto("test",isbn,aut.name(), aut.isni(), LocalDate.now(), aut)));
		
		mockMvc.perform(bookControllerEndPointsMap.findByIsbn(isbn))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(isbn));
		
		verify(bookService, times(1)).findByIsbnNto(isbn);
	}
	
	@Test
	@DisplayName("Test findByTitle endpoint")
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
		
		mockMvc.perform(bookControllerEndPointsMap.findByTitle(title))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(title))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(title));
		
		verify(bookService, times(1)).findByTitleNto(title);
	}
	
	@Test
	@DisplayName("Test searchByTitle endpoint")
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
		
		mockMvc.perform(bookControllerEndPointsMap.searchByTitle(title))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(title))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(title))
						.andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("containsTitle"));
		
		verify(bookService, times(1)).findByTitleContainingIgnoreCaseNto(title);
	}
	
	@ParameterizedTest
	@MethodSource("net.myself.DemoLibrary.Helper.HttpTestCase#addBookTestCases")
	@DisplayName("Test addBook endpoint with various scenarios")
	void addBookScenarios(HttpTestCase testCase) throws Exception {
		BookNto book = BookNto.fromBook(BookHelper.getRandomBook());
		var serialized = jackson.writeValueAsString(book);
		var deserialized = jackson.readValue(serialized, BookNto.class);
		
		BookController_ServiceACT.configureAddBookExpectations(testCase, book, deserialized, bookService);
		
		MvcResult mvcResult = mockMvc.perform(bookControllerEndPointsMap.addBook(serialized)).andReturn();
		
		Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(testCase.expectedStatus.value());
		
		if (testCase.expectedStatus == HttpStatus.OK)
		{
			BookNto addedBook = jackson.readValue(mvcResult.getResponse().getContentAsString(), BookNto.class);
			
			Assertions.assertThat(addedBook.title()).isEqualTo(book.title());
			Assertions.assertThat(addedBook.author()).isEqualTo(book.author());
			Assertions.assertThat(addedBook.isbn()).isEqualTo(book.isbn());
			Assertions.assertThat(addedBook.publishedDate()).isEqualTo(book.publishedDate());
			
			verify(bookService, times(1)).addBookFromNto(deserialized);
		}
	}
	
	@ParameterizedTest
	@MethodSource("net.myself.DemoLibrary.Helper.HttpTestCase#deleteBookByIsbn")
	@DisplayName("Test deleteBook ByIsbn endpoint with various scenarios")
	void deleteBookByIsbn(HttpTestCase testCase) throws Exception
	{
		Book book = BookHelper.getRandomBook();
		
		BookController_ServiceACT.configureDeleteByIsbnExpectations(testCase, book.getIsbn(), bookService);
		
		MvcResult mvcResult = performDeleteByIsbn(book).andReturn();
		
		Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(testCase.expectedStatus.value());
		
		verify(bookService, times(1)).deleteBookByIsbn(book.getIsbn());
	}
	
	@ParameterizedTest
	@MethodSource("net.myself.DemoLibrary.Helper.HttpTestCase#updateBook")
	@DisplayName("Test updateBook endpoint with various scenarios")
	void updateBook(HttpTestCase testCase) throws Exception
	{
		Book book = BookHelper.getRandomBook();
		AuthorNto author = getAuthor();
		BookNto newBook = new BookNto(book.getTitle()+"ed", book.getIsbn(), author.fullName(), author.isni(), LocalDate.now().minus(3, ChronoUnit.YEARS), author);
		
		BookUpdateNto nto = new BookUpdateNto(book.getIsbn(), newBook.title(), newBook.authorIsni(), newBook.publishedDate());
		
		BookController_ServiceACT.configureUpdateBookExpectations(testCase, nto, bookService, newBook);
		
		MvcResult mvcResult = performUpdate(nto).andReturn();
		
		Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(testCase.expectedStatus.value());
		
		if(testCase.expectedStatus == HttpStatus.OK)
		{
			BookNto updatedBook = jackson.readValue(mvcResult.getResponse().getContentAsString(), BookNto.class);
			
			Assertions.assertThat(updatedBook.title()).isEqualTo(newBook.title());
			Assertions.assertThat(updatedBook.authorIsni()).isEqualTo(newBook.authorIsni());
			Assertions.assertThat(updatedBook.isbn()).isEqualTo(book.getIsbn());
			Assertions.assertThat(updatedBook.publishedDate()).isEqualTo(newBook.publishedDate());
		}
		
		verify(bookService, times(1)).updateBookFromNto(nto);
	}
	
	@ParameterizedTest
	@MethodSource("net.myself.DemoLibrary.Helper.HttpTestCase#updateIsbn")
	@DisplayName("Test updateIsbn endpoint with various scenarios")
	void updateIsbn(HttpTestCase testCase) throws Exception
	{
		Book book = BookHelper.getRandomBook();
		
		String isbn = book.getIsbn();
		String toAdd = "edited";
		String newIsbn = isbn.substring(0, isbn.length()-toAdd.length())+toAdd ;
		BookController_ServiceACT.configureUpdateIsbnExpectations(testCase, isbn, newIsbn, bookService);
		
		MvcResult mvcResult = performUpdateIsbn(isbn, newIsbn).andReturn();
		
		Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(testCase.expectedStatus.value());
	}
	
	private static AuthorNto getAuthor()
	{
		return new AuthorNto("AAAAAAAAAAAAAAAA", "test", "test", "test test", LocalDate.now());
	}
	
	private ResultActions performDeleteByIsbn(Book book) throws Exception
	{
		return mockMvc.perform(bookControllerEndPointsMap.deleteBookByIsbn(book)).andDo(print());
	}
	
	private ResultActions performUpdate(BookUpdateNto nto) throws Exception
	{
		return mockMvc.perform(bookControllerEndPointsMap.updateBook(jackson.writeValueAsString(nto))).andDo(print());
	}
	
	private ResultActions performUpdateIsbn(String isbn, String newIsbn) throws Exception
	{
		return mockMvc.perform(bookControllerEndPointsMap.updateIsbn(isbn, newIsbn)).andDo(print());
	}
}