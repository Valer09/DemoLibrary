package net.myself.DemoLibrary.Unit.Controller;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.myself.DemoLibrary.Controller.BookController;
import net.myself.DemoLibrary.Data.Entities.Book;
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
	private IBookRepository bookRepository;
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
		
		when(bookService.getAllBooks()).thenReturn(bookList);
		
		MvcResult mvcResult = mockMvc.perform(BookControllerRequestMap.getAllBooks())
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)))
						.andReturn();
		
		// Serialize the response and using Assertions.[...]. For an example of direct access and assertions on json response fields values, look findByIsbn test
		List<Book> response = jackson.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
		for(int i = 0; i < response.size(); i++)
		{
			Book b = response.get(i);
			Assertions.assertThat(b.getId()).isEqualTo(bookList.get(i).getId());
			Assertions.assertThat(b.getIsbn()).isEqualTo(bookList.get(i).getIsbn());
			Assertions.assertThat(b.getTitle()).isEqualTo(bookList.get(i).getTitle());
			Assertions.assertThat(b.getAuthor()).isEqualTo(bookList.get(i).getAuthor());
		}
		
		verify(bookService, times(1)).getAllBooks();
	}
	
	@Test
	void findByIsbn() throws Exception
	{
		String isbn = "abcd";
		
		when(bookService.findByIsbn(isbn)).thenReturn(Optional.of(new Book(1,"test","test", isbn,LocalDate.now())));
		
		mockMvc.perform(BookControllerRequestMap.findByIbsn(isbn))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(isbn));
		
		verify(bookService, times(1)).findByIsbn(isbn);
	}
	
	@Test
	void findByTitle() throws Exception
	{
		String title = "title";
		List<Book> books = new ArrayList<>(Arrays.asList
						(
										new Book(1, title,"author","abcd",LocalDate.now()),
										new Book(1, title,"author","zxy",LocalDate.now())
						));
		
		when(bookService.findByTitle(title)).thenReturn(books);
		
		mockMvc.perform(BookControllerRequestMap.findByTitle(title))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(title))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(title));
		
		verify(bookService, times(1)).findByTitle(title);
	}
	
	@Test
	void searchByTitle() throws Exception
	{
		String title = "Title";
		List<Book> books = new ArrayList<>(Arrays.asList
						(
							new Book(1,title,"author","abcd",LocalDate.now()),
							new Book(1,title,"author","zxy",LocalDate.now()),
							new Book(1,"contains"+title,"author2","zxy",LocalDate.now())
						));
		
		when(bookService.findByTitleContainingIgnoreCase(title)).thenReturn(books);
		
		mockMvc.perform(BookControllerRequestMap.searchByTitle(title))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
						.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(title))
						.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(title))
						.andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("containsTitle"));
		
		verify(bookService, times(1)).findByTitleContainingIgnoreCase(title);
	}
	
	@Test
	void addBook() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		
		when(bookService.addBook(book)).thenReturn(ServiceResponse.createOk(book));
		
		MvcResult mvcResult = mockMvc.perform(BookControllerRequestMap.addBook(jackson.writeValueAsString(book)))
						.andDo(print())
						.andExpect(status().isCreated())
						.andReturn();
		
		Book addedBook = jackson.readValue(mvcResult.getResponse().getContentAsString(), Book.class);
		
		Assertions.assertThat(addedBook.getTitle()).isEqualTo(book.getTitle());
		Assertions.assertThat(addedBook.getAuthor()).isEqualTo(book.getAuthor());
		Assertions.assertThat(addedBook.getIsbn()).isEqualTo(book.getIsbn());
		Assertions.assertThat(addedBook.getPublishedDate()).isEqualTo(book.getPublishedDate());
		
		verify(bookService, times(1)).addBook(book);
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
	void deleteBookById() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		
		when(bookService.deleteBookById(book.getId())).thenReturn(ServiceResponse.createOk(""));
		
		performDeleteById(book).andExpect(status().isOk());
		
		verify(bookService, times(1)).deleteBookById(book.getId());
	}
	
	@Test
	void deleteBookByIdNotFound() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		
		when(bookService.deleteBookById(book.getId())).thenReturn(ServiceResponse.createError(ServiceResult.NOT_FOUND, ""));
		
		performDeleteById(book).andExpect(status().isNotFound());
		
		verify(bookService, times(1)).deleteBookById(book.getId());
	}
	
	@Test
	void deleteBook() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		
		String jsonBook = jackson.writeValueAsString(book);
		
		when(bookService.deleteBook(book)).thenReturn(ServiceResponse.createOk(""));
		
		performDeleteBook(jsonBook).andExpect(status().isOk());
		
		verify(bookService, times(1)).deleteBook(book);
	}
	
	@Test
	void deleteBookConflict() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		
		when(bookService.deleteBook(book)).thenReturn(ServiceResponse.createError(ServiceResult.CONFLICT,""));
		
		performDeleteBook(jackson.writeValueAsString(book)).andExpect(status().isConflict());
		
		verify(bookService, times(1)).deleteBook(book);
	}
	
	@Test
	void deleteBookNotFound() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		
		when(bookService.deleteBook(book)).thenReturn(ServiceResponse.createError(ServiceResult.NOT_FOUND, ""));
		
		performDeleteBook(jackson.writeValueAsString(book)).andExpect(status().isNotFound());
		
		verify(bookService, times(1)).deleteBook(book);
	}
	
	@Test
	void updateBook() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		Book newBook = new Book(book.getId(), book.getTitle()+"ed", book.getAuthor()+"ed", book.getIsbn(), book.getPublishedDate());
		BookUpdateNto nto = new BookUpdateNto(book, newBook);
		
		when(bookService.updateBook(nto)).thenReturn(ServiceResponse.createOk(newBook));
		
		MvcResult mvcResult = performUpdate(nto).andExpect(status().isOk()).andReturn();
		
		Book updatedBook = jackson.readValue(mvcResult.getResponse().getContentAsString(), Book.class);
		
		Assertions.assertThat(updatedBook.getTitle()).isEqualTo(newBook.getTitle());
		Assertions.assertThat(updatedBook.getAuthor()).isEqualTo(newBook.getAuthor());
		Assertions.assertThat(updatedBook.getIsbn()).isEqualTo(newBook.getIsbn());
		Assertions.assertThat(updatedBook.getPublishedDate()).isEqualTo(newBook.getPublishedDate());
		
		verify(bookService, times(1)).updateBook(nto);
	}
	
	@Test
	void updateBookConflict() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		Book newBook = new Book(book.getId(), book.getTitle()+"ed", book.getAuthor()+"ed", book.getIsbn(), book.getPublishedDate());
		BookUpdateNto nto = new BookUpdateNto(book, newBook);
		
		when(bookService.updateBook(nto)).thenReturn(ServiceResponse.createError(ServiceResult.CONFLICT, ""));
		
		performUpdate(nto).andExpect(status().isConflict());
		
		verify(bookService, times(1)).updateBook(nto);
	}
	
	@Test
	void updateBookNotFound() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		Book newBook = new Book(book.getId(), book.getTitle()+"ed", book.getAuthor()+"ed", book.getIsbn(), book.getPublishedDate());
		BookUpdateNto nto = new BookUpdateNto(book, newBook);
		
		when(bookService.updateBook(nto)).thenReturn(ServiceResponse.createError(ServiceResult.NOT_FOUND, ""));
		
		performUpdate(nto).andExpect(status().isNotFound());
		
		verify(bookService, times(1)).updateBook(nto);
	}
	
	private ResultActions performDeleteByIsbn(Book book) throws Exception
	{
		return mockMvc.perform(BookControllerRequestMap.deleteBookByIsbn(book)).andDo(print());
	}
	
	private ResultActions performDeleteById(Book book) throws Exception
	{
		return mockMvc.perform(BookControllerRequestMap.deleteBookById(book)).andDo(print());
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