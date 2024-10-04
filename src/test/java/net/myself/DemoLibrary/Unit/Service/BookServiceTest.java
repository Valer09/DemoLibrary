package net.myself.DemoLibrary.Unit.Service;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Helper.BookHelper;
import net.myself.DemoLibrary.Model.BookUpdate;
import net.myself.DemoLibrary.Service.AuthorService;
import net.myself.DemoLibrary.Service.BookService;
import net.myself.DemoLibrary.Service.ServiceResponse;
import net.myself.DemoLibrary.Service.ServiceResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest
{
	@Autowired
	private BookService bookService;
	@MockBean
	private IBookRepository bookRepositoryMock;
	@MockBean
	private AuthorService authorServiceMock;
	
	@Test
	void getAllBooksTest()
	{
		List<Book> bookList = new ArrayList<>(Arrays.asList(
						BookHelper.getRandomBook(),
						BookHelper.getRandomBook()));
		
		when(bookRepositoryMock.findAll()).thenReturn(bookList);
		
		List<BookNto> result = bookService.getAllBooksNto();
		Assertions.assertThat(result.get(0).isbn()).isEqualTo(bookList.get(0).getIsbn());
		Assertions.assertThat(result.get(1).isbn()).isEqualTo(bookList.get(1).getIsbn());
		
		verify(bookRepositoryMock, times(1)).findAll();
	}
	
	@Test
	void findByIsbn()
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepositoryMock.findByIsbn(book.getIsbn())).thenReturn(Optional.of(book));
		var foundBook = bookService.findByIsbnNto(book.getIsbn());
		assertEquals(foundBook.get().isbn(), book.getIsbn());
		assertEquals(foundBook.get().title(), book.getTitle());
		verify(bookRepositoryMock, times(1)).findByIsbn(book.getIsbn());
	}
	
	@Test
	void findByTitle()
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepositoryMock.findByTitle(book.getTitle())).thenReturn(new ArrayList<>(List.of(book)));
		var foundBook = bookService.findByTitleNto(book.getTitle()).get(0);
		assertEquals(foundBook.isbn(), book.getIsbn());
		verify(bookRepositoryMock, times(1)).findByTitle(book.getTitle());
	}
	
	@Test
	void searchByTitle()
	{
		var auth = getAuthor();
		var authFullName = getAuthorFullName(auth);
		List<Book> list = new ArrayList<>(Arrays.asList(
						Book.createTransientBook(new BookNto("title", "a", authFullName, auth.isni(), LocalDate.now(), auth)),
						Book.createTransientBook(new BookNto("titleOnStart","b", authFullName, auth.isni(), LocalDate.now(), auth)),
						Book.createTransientBook(new BookNto("EndWithtitle","c", authFullName, auth.isni(), LocalDate.now(), auth)),
						Book.createTransientBook(new BookNto("InTheMiddletitleIs","d", authFullName, auth.isni(), LocalDate.now(), auth)),
						Book.createTransientBook(new BookNto("TITLEisuppercase","e", authFullName, auth.isni(), LocalDate.now(), auth))));
		
		when(bookRepositoryMock.findByTitleContainingIgnoreCase("title")).thenReturn(list);
		
		var result = bookService.findByTitleContainingIgnoreCaseNto("title");
		assertEquals(result.get(0).isbn(), list.get(0).getIsbn());
		assertEquals(result.get(1).isbn(), list.get(1).getIsbn());
		assertEquals(result.get(2).isbn(), list.get(2).getIsbn());
		assertEquals(result.get(3).isbn(), list.get(3).getIsbn());
		assertEquals(result.get(4).isbn(), list.get(4).getIsbn());
		
		verify(bookRepositoryMock, times(1)).findByTitleContainingIgnoreCase("title");
	}
	
	@Test
	void addBook()
	{
		
		Book book = BookHelper.getRandomBook();
		when(bookRepositoryMock.save(any(Book.class))).thenReturn(book);
		when(bookRepositoryMock.existsByIsbn(book.getIsbn())).thenReturn(false);
		when(authorServiceMock.existsByIsni(book.getAuthor().getIsni())).thenReturn(true);
		when(authorServiceMock.findAuthorByIsni(book.getAuthor().getIsni())).thenReturn(ServiceResponse.createOk(book.getAuthor()));
		
		BookNto addedBookNto = bookService.addBookFromNto(BookNto.fromBook(book)).get();
		assertEquals(book.getIsbn(), addedBookNto.isbn());
		
		ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
		verify(bookRepositoryMock, times(1)).save(bookCaptor.capture());
		
		Book capturedBook = bookCaptor.getValue();
		
		assertEquals(book.getIsbn(), capturedBook.getIsbn());
		assertEquals(book.getTitle(), capturedBook.getTitle());
		assertEquals(book.getAuthor(), capturedBook.getAuthor());
		assertEquals(book.getPublishedDate(), capturedBook.getPublishedDate());
	}
	
	@Test
	void deleteBookByIsbn()
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepositoryMock.existsByIsbn(book.getIsbn())).thenReturn(true);
		Assertions.assertThat(bookService.deleteBookByIsbn(book.getIsbn()).getResult()).isEqualTo(ServiceResult.OK);
		verify(bookRepositoryMock, times(1)).deleteByIsbn(book.getIsbn());
	}
	
	@Test
	void deleteBookByIsbnNotFound()
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepositoryMock.existsByIsbn(book.getIsbn())).thenReturn(false);
		Assertions.assertThat(bookService.deleteBookByIsbn(book.getIsbn()).getResult()).isEqualTo(ServiceResult.NOT_FOUND);
		verify(bookRepositoryMock, never()).deleteByIsbn(any());
	}
	
	@Test
	void deleteBookById()
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepositoryMock.existsById(book.getId())).thenReturn(true);
		Assertions.assertThat(bookService.deleteBookById(book.getId()).getResult()).isEqualTo(ServiceResult.OK);
		verify(bookRepositoryMock, times(1)).deleteById(book.getId());
	}
	
	@Test
	void deleteBookByIdNotFound()
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepositoryMock.existsById(book.getId())).thenReturn(false);
		Assertions.assertThat(bookService.deleteBookById(book.getId()).getResult()).isEqualTo(ServiceResult.NOT_FOUND);
		verify(bookRepositoryMock, never()).deleteById(any());
	}
	
	@Test
	void updateBook()
	{
		Book book = BookHelper.getRandomBook();
		Book temp = BookHelper.getRandomBook();
		Book updatedBook =  Book.createTransientBook(BookNto.fromBook(book));
		updatedBook.update(new BookUpdate(temp.getTitle(), temp.getAuthor(), temp.getPublishedDate()));
		Optional<Book> optionalBookCopy = Optional.of(book);
		
		when(bookRepositoryMock.findByIsbn(book.getIsbn())).thenReturn(optionalBookCopy);
		when(bookRepositoryMock.save(optionalBookCopy.get())).thenReturn(updatedBook);
		when(authorServiceMock.existsByIsni(temp.getAuthor().getIsni())).thenReturn(true);
		when(authorServiceMock.findAuthorByIsni(temp.getAuthor().getIsni())).thenReturn(ServiceResponse.createOk(temp.getAuthor()));
		
		var result = bookService.updateBookFromNto(new BookUpdateNto(book.getIsbn(), temp.getTitle(), temp.getAuthor().getIsni(), temp.getPublishedDate()));
		Assertions.assertThat(result.getResult()).isEqualTo(ServiceResult.OK);
		assertEquals(result.get().isbn(), book.getIsbn());
		assertEquals(result.get().title(), temp.getTitle());
		assertEquals(result.get().authorIsni(), temp.getAuthor().getIsni());
		assertEquals(result.get().publishedDate(), temp.getPublishedDate());
		verify(bookRepositoryMock, times(1)).save(book);
	}
	
	@Test
	void updateBookAuthorNotFound()
	{
		Book book = BookHelper.getRandomBook();
		Book temp = BookHelper.getRandomBook();
		Book bookCopy =  Book.createTransientBook(BookNto.fromBook(book));
		book.update(new BookUpdate(temp.getTitle(), temp.getAuthor(), temp.getPublishedDate()));
		
		when(bookRepositoryMock.findByIsbn(book.getIsbn())).thenReturn(Optional.of(bookCopy));
		when(authorServiceMock.existsByIsni(temp.getAuthor().getIsni())).thenReturn(false);
		
		var result = bookService.updateBookFromNto(new BookUpdateNto(book.getIsbn(), temp.getTitle(), temp.getAuthor().getIsni(), temp.getPublishedDate()));
		
		Assertions.assertThat(result.getResult()).isEqualTo(ServiceResult.SERVER_ERROR);
		
		verify(authorServiceMock,never()).findAuthorByIsni(any());
		verify(bookRepositoryMock,never()).save(any());
	}
	
	@Test
	void updateBookNotFound()
	{
		Book book = BookHelper.getRandomBook();
		
		when(bookRepositoryMock.findByIsbn(book.getIsbn())).thenReturn(Optional.empty());
		
		var result = bookService.updateBookFromNto(new BookUpdateNto(book.getIsbn(), book.getTitle(), book.getAuthor().getIsni(), book.getPublishedDate()));
		Assertions.assertThat(result.getResult()).isEqualTo(ServiceResult.NOT_FOUND);
		assertNull(result.get());
		verify(bookRepositoryMock, never()).save(any());
	}
	
	private static AuthorNto getAuthor()
	{
		return new AuthorNto("g", "test", "test", "test"+" "+"test", LocalDate.now());
	}
	
	private static String getAuthorFullName(AuthorNto author)
	{
		return author.name() + " " + author.lastName();
	}
}
