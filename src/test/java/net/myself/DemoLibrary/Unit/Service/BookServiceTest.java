package net.myself.DemoLibrary.Unit.Service;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BookServiceTest
{
	@Autowired
	private BookService bookService;
	@MockBean
	private IBookRepository bookRepository;
	
	@Test
	void getAllBooksTest()
	{
		List<Book> bookList = new ArrayList<>(Arrays.asList(
						BookHelper.getRandomBook(),
						BookHelper.getRandomBook()));
		
		when(bookRepository.findAll()).thenReturn(bookList);
		
		List<Book> result = bookService.getAllBooks();
		Assertions.assertThat(result.get(0).getId()).isEqualTo(bookList.get(0).getId());
		Assertions.assertThat(result.get(1).getId()).isEqualTo(bookList.get(1).getId());
		
		verify(bookRepository, times(1)).findAll();
	}
	
	@Test
	void findByIsbn() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.of(book));
		var foundBook = bookService.findByIsbn(book.getIsbn());
		assertEquals(foundBook.get().getId(), book.getId());
		verify(bookRepository, times(1)).findByIsbn(book.getIsbn());
	}
	
	@Test
	void findByTitle() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepository.findByTitle(book.getTitle())).thenReturn(new ArrayList<>(Arrays.asList(book)));
		var foundBook = bookService.findByTitle(book.getTitle()).get(0);
		assertEquals(foundBook.getId(), book.getId());
		verify(bookRepository, times(1)).findByTitle(book.getTitle());
	}
	
	@Test
	void searchByTitle() throws Exception
	{
		List<Book> list = new ArrayList<>(Arrays.asList(
						new Book(1, "title", "test", "test", LocalDate.now()),
						new Book(2, "titleOnStart", "test", "test", LocalDate.now()),
						new Book(3, "EndWithtitle", "test", "test", LocalDate.now()),
						new Book(4, "InTheMiddletitleIs", "test", "test", LocalDate.now()),
						new Book(5, "TITLEisuppercase", "test", "test", LocalDate.now())));
		
		when(bookRepository.findByTitleContaining("title")).thenReturn(list);
		
		var result = bookService.findByTitleContaining("title");
		assertEquals(result.get(0).getId(), list.get(0).getId());
		assertEquals(result.get(1).getId(), list.get(1).getId());
		assertEquals(result.get(2).getId(), list.get(2).getId());
		assertEquals(result.get(3).getId(), list.get(3).getId());
		assertEquals(result.get(4).getId(), list.get(4).getId());
		
		verify(bookRepository, times(1)).findByTitleContaining("title");
	}
	
	@Test
	void addBook() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepository.save(book)).thenReturn(book);
		assertEquals(bookService.addBook(book).get().getId(), book.getId());
		verify(bookRepository, times(1)).save(book);
	}
	
	@Test
	void deleteBookByIsbn() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(true);
		Assertions.assertThat(bookService.deleteBookByIsbn(book.getIsbn()).getResult()).isEqualTo(ServiceResult.OK);
		verify(bookRepository, times(1)).deleteByIsbn(book.getIsbn());
	}
	
	@Test
	void deleteBookByIsbnNotFound() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(false);
		Assertions.assertThat(bookService.deleteBookByIsbn(book.getIsbn()).getResult()).isEqualTo(ServiceResult.NOT_FOUND);
		verify(bookRepository, never()).deleteByIsbn(book.getIsbn());
	}
	
	@Test
	void deleteBookById() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepository.existsById(book.getId())).thenReturn(true);
		Assertions.assertThat(bookService.deleteBookById(book.getId()).getResult()).isEqualTo(ServiceResult.OK);
		verify(bookRepository, times(1)).deleteById(book.getId());
	}
	
	@Test
	void deleteBookByIdNotFound() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepository.existsById(book.getId())).thenReturn(false);
		Assertions.assertThat(bookService.deleteBookById(book.getId()).getResult()).isEqualTo(ServiceResult.NOT_FOUND);
		verify(bookRepository, never()).deleteById(book.getId());
	}
	
	@Test
	void deleteBook() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book)));
		Assertions.assertThat(bookService.deleteBook(book).getResult()).isEqualTo(ServiceResult.OK);
		verify(bookRepository, times(1)).delete(book);
	}
	
	@Test
	void deleteBookConflict() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book, book)));
		Assertions.assertThat(bookService.deleteBook(book).getResult()).isEqualTo(ServiceResult.CONFLICT);
		verify(bookRepository, never()).delete(book);
	}
	
	@Test
	void deleteBookNotFound() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>());
		Assertions.assertThat(bookService.deleteBook(book).getResult()).isEqualTo(ServiceResult.NOT_FOUND);
		verify(bookRepository, never()).delete(book);
	}
	
	@Test
	void updateBook() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		Book temp = BookHelper.getRandomBook();
		Book bookCopy =  new Book(book.getId(), temp.getTitle(), temp.getAuthor(), temp.getIsbn(), temp.getPublishedDate());
		Book bookNto = new Book(0, temp.getTitle(), temp.getAuthor(), temp.getIsbn(), temp.getPublishedDate());
		bookCopy.update(bookNto);
		
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book)));
		when(bookRepository.save(book)).thenReturn(bookCopy);
		
		var result = bookService.updateBook(new BookUpdateNto(book, bookNto));
		Assertions.assertThat(result.getResult()).isEqualTo(ServiceResult.OK);
		assertEquals(result.get().getId(), book.getId());
		assertEquals(result.get().getTitle(), bookCopy.getTitle());
		assertEquals(result.get().getAuthor(), bookCopy.getAuthor());
		assertEquals(result.get().getIsbn(), bookCopy.getIsbn());
		assertEquals(result.get().getPublishedDate(), bookCopy.getPublishedDate());
		verify(bookRepository, times(1)).save(book);
	}
	
	@Test
	void updateBookConflict() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		Book bookEdit = new Book(BookHelper.getRandomBook().getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublishedDate());
		bookEdit.update(BookHelper.getRandomBook());
		
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book, book)));
		
		var result = bookService.updateBook(new BookUpdateNto(book, bookEdit));
		Assertions.assertThat(result.getResult()).isEqualTo(ServiceResult.CONFLICT);
		assertNull(result.get());
		verify(bookRepository, never()).save(book);
	}
	
	@Test
	void updateBookNotFound() throws Exception
	{
		Book book = BookHelper.getRandomBook();
		Book bookEdit = new Book(BookHelper.getRandomBook().getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublishedDate());
		bookEdit.update(BookHelper.getRandomBook());
		
		when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of()));
		
		var result = bookService.updateBook(new BookUpdateNto(book, bookEdit));
		Assertions.assertThat(result.getResult()).isEqualTo(ServiceResult.NOT_FOUND);
		assertNull(result.get());
		verify(bookRepository, never()).save(book);
	}
}
