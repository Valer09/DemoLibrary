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
		// when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(new Book(1,"test","test", isbn,LocalDate.now())));
		
		//verify(bookRepository, times(1)).findByIsbn(isbn);
	}
	
	@Test
	void findByTitle() throws Exception
	{
		//when(bookRepository.findByTitle(title)).thenReturn(books);
		
		//verify(bookRepository, times(1)).findByTitle(title);
	}
	
	@Test
	void searchByTitle() throws Exception
	{
		//when(bookRepository.findByTitleContaining(title)).thenReturn(books);
		
		//verify(bookRepository, times(1)).findByTitleContaining(title);
	}
	
	@Test
	void addBook() throws Exception
	{
		//when(bookRepository.save(book)).thenReturn(book);
		
		//verify(bookRepository, times(1)).save(book);
	}
	
	@Test
	void deleteBookByIsbn() throws Exception
	{
		//when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(true);
		
		//verify(bookRepository, times(1)).deleteByIsbn(book.getIsbn());
	}
	
	@Test
	void deleteBookByIsbnNotFound() throws Exception
	{
		//when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(false);
		
		//verify(bookRepository, never()).deleteByIsbn(book.getIsbn());
	}
	
	@Test
	void deleteBookById() throws Exception
	{
		//when(bookRepository.existsById(book.getId())).thenReturn(true);
		
		//verify(bookRepository, times(1)).deleteById(book.getId());
	}
	
	@Test
	void deleteBookByIdNotFound() throws Exception
	{
		//when(bookRepository.existsById(book.getId())).thenReturn(false);
		
		//verify(bookRepository, never()).deleteById(book.getId());
	}
	
	@Test
	void deleteBook() throws Exception
	{
		//when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book)));
		
		//verify(bookRepository, times(1)).delete(book);
	}
	
	@Test
	void deleteBookConflict() throws Exception
	{
		//when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book, book)));
		
		//verify(bookRepository, never()).delete(book);
	}
	
	@Test
	void deleteBookNotFound() throws Exception
	{
		//when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>());
		
		//verify(bookRepository, never()).delete(book);
	}
	
	@Test
	void updateBook() throws Exception
	{
		//when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book)));
		
		//verify(bookRepository, times(1)).save(book);
	}
	
	@Test
	void updateBookConflict() throws Exception
	{
		//when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book, book)));
		
		//verify(bookRepository, never()).save(book);
	}
	
	@Test
	void updateBookNotFound() throws Exception
	{
		//when(bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of()));
		
		//verify(bookRepository, never()).save(book);
	}
}
