package net.myself.DemoLibrary.Unit.Controller;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.myself.DemoLibrary.Controller.BookController;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Helper.BookControllerRequestMap;
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
		
		MvcResult mvcResult = mockMvc.perform(BookControllerRequestMap.getAllBooks())
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
			Assertions.assertThat(b.author().cf()).isEqualTo(bookList.get(i).getAuthor().getCf());
		}
		
		verify(bookService, times(1)).getAllBooksNto();
	}
	
	@Test
	void findByIsbn() throws Exception
	{
		String isbn = "abcd";
		
		when(bookService.findByIsbnNto(isbn)).thenReturn(Optional.of(new BookNto("test",getAuthor(), isbn,LocalDate.now())));
		
		mockMvc.perform(BookControllerRequestMap.findByIbsn(isbn))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(isbn));
		
		verify(bookService, times(1)).findByIsbnNto(isbn);
	}
	
	@Test
	void findByTitle() throws Exception
	{
		String title = "title";
		List<BookNto> books = new ArrayList<>(Arrays.asList
						(
										new BookNto(title,getAuthor(),"abcd",LocalDate.now()),
										new BookNto(title,getAuthor(),"zxy",LocalDate.now())
						));
		
		when(bookService.findByTitleNto(title)).thenReturn(books);
		
		mockMvc.perform(BookControllerRequestMap.findByTitle(title))
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
		List<BookNto> books = new ArrayList<>(Arrays.asList
						(
										new BookNto(title,getAuthor(),"abcd",LocalDate.now()),
										new BookNto(title,getAuthor(),"zxy",LocalDate.now()),
										new BookNto("contains"+title,getAuthor(),"zxy",LocalDate.now())
						));
		
		when(bookService.findByTitleContainingIgnoreCaseNto(title)).thenReturn(books);
		
		mockMvc.perform(BookControllerRequestMap.searchByTitle(title))
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
		
		when(bookService.addBookFromNto(book)).thenReturn(ServiceResponse.createOk(book));
		
		MvcResult mvcResult = mockMvc.perform(BookControllerRequestMap.addBook(jackson.writeValueAsString(book)))
						.andDo(print())
						.andExpect(status().isCreated())
						.andReturn();
		
		BookNto addedBook = jackson.readValue(mvcResult.getResponse().getContentAsString(), BookNto.class);
		
		Assertions.assertThat(addedBook.title()).isEqualTo(book.title());
		Assertions.assertThat(addedBook.author()).isEqualTo(book.author());
		Assertions.assertThat(addedBook.isbn()).isEqualTo(book.isbn());
		Assertions.assertThat(addedBook.publishedDate()).isEqualTo(book.publishedDate());
		
		verify(bookService, times(1)).addBookFromNto(book);
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
	void deleteBook() throws Exception
	{
		BookNto book = BookNto.fromBook(BookHelper.getRandomBook());
		
		String jsonBook = jackson.writeValueAsString(book);
		
		when(bookService.deleteBookFromNto(book)).thenReturn(ServiceResponse.createOk(""));
		
		performDeleteBook(jsonBook).andExpect(status().isOk());
		
		verify(bookService, times(1)).deleteBookFromNto(book);
	}
	
	@Test
	void deleteBookConflict() throws Exception
	{
		BookNto book = BookNto.fromBook(BookHelper.getRandomBook());
		
		when(bookService.deleteBookFromNto(book)).thenReturn(ServiceResponse.createError(ServiceResult.CONFLICT,""));
		
		performDeleteBook(jackson.writeValueAsString(book)).andExpect(status().isConflict());
		
		verify(bookService, times(1)).deleteBookFromNto(book);
	}
	
	@Test
	void deleteBookNotFound() throws Exception
	{
		BookNto book = BookNto.fromBook(BookHelper.getRandomBook());
		
		when(bookService.deleteBookFromNto(book)).thenReturn(ServiceResponse.createError(ServiceResult.NOT_FOUND, ""));
		
		performDeleteBook(jackson.writeValueAsString(book)).andExpect(status().isNotFound());
		
		verify(bookService, times(1)).deleteBookFromNto(book);
	}
	
	@Test
	void updateBook() throws Exception
	{
		BookNto book = BookNto.fromBook(BookHelper.getRandomBook());
		BookNto newBook = new BookNto(book.title()+"ed", getAuthor(), book.isbn(), book.publishedDate());
		BookUpdateNto nto = new BookUpdateNto(book, newBook);
		
		when(bookService.updateBookFromNto(nto)).thenReturn(ServiceResponse.createOk(newBook));
		
		MvcResult mvcResult = performUpdate(nto).andExpect(status().isOk()).andReturn();
		
		Book updatedBook = jackson.readValue(mvcResult.getResponse().getContentAsString(), Book.class);
		
		Assertions.assertThat(updatedBook.getTitle()).isEqualTo(newBook.title());
		Assertions.assertThat(updatedBook.getAuthor().getCf()).isEqualTo(newBook.author().cf());
		Assertions.assertThat(updatedBook.getIsbn()).isEqualTo(newBook.isbn());
		Assertions.assertThat(updatedBook.getPublishedDate()).isEqualTo(newBook.publishedDate());
		
		verify(bookService, times(1)).updateBookFromNto(nto);
	}
	
	@Test
	void updateBookConflict() throws Exception
	{
		BookNto book = BookNto.fromBook(BookHelper.getRandomBook());
		BookNto newBook = new BookNto(book.title()+"ed", getAuthor(), book.isbn(), book.publishedDate());
		BookUpdateNto nto = new BookUpdateNto(book, newBook);
		
		when(bookService.updateBookFromNto(nto)).thenReturn(ServiceResponse.createError(ServiceResult.CONFLICT, ""));
		
		performUpdate(nto).andExpect(status().isConflict());
		
		verify(bookService, times(1)).updateBookFromNto(nto);
	}
	
	@Test
	void updateBookNotFound() throws Exception
	{
		BookNto book = BookNto.fromBook(BookHelper.getRandomBook());
		BookNto newBook = new BookNto(book.title()+"ed", getAuthor(), book.isbn(), book.publishedDate());
		BookUpdateNto nto = new BookUpdateNto(book, newBook);
		
		when(bookService.updateBookFromNto(nto)).thenReturn(ServiceResponse.createError(ServiceResult.NOT_FOUND, ""));
		
		performUpdate(nto).andExpect(status().isNotFound());
		
		verify(bookService, times(1)).updateBookFromNto(nto);
	}
	
	private static AuthorNto getAuthor()
	{
		return new AuthorNto("A", "test", "test", LocalDate.now());
	}
	
	private ResultActions performDeleteByIsbn(Book book) throws Exception
	{
		return mockMvc.perform(BookControllerRequestMap.deleteBookByIsbn(book)).andDo(print());
	}
	
	private ResultActions performDeleteBook(String jsonBook) throws Exception
	{
		return mockMvc.perform(BookControllerRequestMap.deleteBook(jsonBook)).andDo(print());
	}
	
	private ResultActions performUpdate(BookUpdateNto nto) throws Exception
	{
		return mockMvc.perform(BookControllerRequestMap.updateBook(jackson.writeValueAsString(nto))).andDo(print());
	}
}