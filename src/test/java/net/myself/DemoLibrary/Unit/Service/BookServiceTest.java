package net.myself.DemoLibrary.Unit.Service;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Data.Repository.IAuthorRepository;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Helper.BookHelper;
import net.myself.DemoLibrary.Service.AuthorService;
import net.myself.DemoLibrary.Service.BookService;
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
		when(bookRepositoryMock.findByTitle(book.getTitle())).thenReturn(new ArrayList<>(Arrays.asList(book)));
		var foundBook = bookService.findByTitleNto(book.getTitle()).get(0);
		assertEquals(foundBook.isbn(), book.getIsbn());
		verify(bookRepositoryMock, times(1)).findByTitle(book.getTitle());
	}
	
	@Test
	void searchByTitle()
	{
		List<Book> list = new ArrayList<>(Arrays.asList(
						Book.createTransientBook(new BookNto("title", getAuthor(), "test", LocalDate.now())),
						Book.createTransientBook(new BookNto("titleOnStart", getAuthor(), "test", LocalDate.now())),
						Book.createTransientBook(new BookNto("EndWithtitle", getAuthor(), "test", LocalDate.now())),
						Book.createTransientBook(new BookNto("InTheMiddletitleIs", getAuthor(), "test", LocalDate.now())),
						Book.createTransientBook(new BookNto("TITLEisuppercase", getAuthor(), "test", LocalDate.now()))));
		
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
		when(authorServiceMock.existsByCf(book.getAuthor().getCf())).thenReturn(true);
		when(authorServiceMock.findAuthorByCf(book.getAuthor().getCf())).thenReturn(Optional.of(book.getAuthor()));
		
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
	void deleteBook()
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepositoryMock.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book)));
		Assertions.assertThat(bookService.deleteBookFromNto(BookNto.fromBook(book)).getResult()).isEqualTo(ServiceResult.OK);
		verify(bookRepositoryMock, times(1)).delete(book);
	}
	
	@Test
	void deleteBookConflict()
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepositoryMock.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book, book)));
		Assertions.assertThat(bookService.deleteBookFromNto(BookNto.fromBook(book)).getResult()).isEqualTo(ServiceResult.CONFLICT);
		verify(bookRepositoryMock, never()).delete(any());
	}
	
	@Test
	void deleteBookNotFound()
	{
		Book book = BookHelper.getRandomBook();
		when(bookRepositoryMock.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>());
		Assertions.assertThat(bookService.deleteBookFromNto(BookNto.fromBook(book)).getResult()).isEqualTo(ServiceResult.NOT_FOUND);
		verify(bookRepositoryMock, never()).delete(any());
	}
	
	@Test
	void updateBook()
	{
		Book book = BookHelper.getRandomBook();
		Book temp = BookHelper.getRandomBook();
		Book bookCopy =  Book.createTransientBook(BookNto.fromBook(book));
		bookCopy.update(temp);
		BookNto bookNto = BookNto.fromBook(temp);
		
		
		when(bookRepositoryMock.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book)));
		when(bookRepositoryMock.save(book)).thenReturn(bookCopy);
		when(authorServiceMock.existsByCf(bookCopy.getAuthor().getCf())).thenReturn(true);
		when(authorServiceMock.findAuthorByCf(bookCopy.getAuthor().getCf())).thenReturn(Optional.of(bookCopy.getAuthor()));
		
		var result = bookService.updateBookFromNto(new BookUpdateNto(BookNto.fromBook(book), bookNto));
		Assertions.assertThat(result.getResult()).isEqualTo(ServiceResult.OK);
		assertEquals(result.get().isbn(), book.getIsbn());
		assertEquals(result.get().title(), bookCopy.getTitle());
		assertEquals(result.get().author().cf(), bookCopy.getAuthor().getCf());
		assertEquals(result.get().isbn(), bookCopy.getIsbn());
		assertEquals(result.get().publishedDate(), bookCopy.getPublishedDate());
		verify(bookRepositoryMock, times(1)).save(book);
	}
	
	@Test
	void updateBookWithDifferentAuthor()
	{
		Book book = BookHelper.getRandomBook();
		BookNto oldBookNto =  BookNto.fromBook(book);
		book.update(BookHelper.getRandomBook());
		BookUpdateNto bookUpdateNto = new BookUpdateNto(oldBookNto,  BookNto.fromBook(book));
		
		when(bookRepositoryMock.findByTitleAndIsbn(bookUpdateNto.oldBook().title(), bookUpdateNto.oldBook().isbn())).thenReturn(new ArrayList<>(List.of(book)));
		when(authorServiceMock.existsByCf(book.getAuthor().getCf())).thenReturn(true);
		when(authorServiceMock.findAuthorByCf(book.getAuthor().getCf())).thenReturn(Optional.of(book.getAuthor()));
		when(bookRepositoryMock.save(book)).thenReturn(book);
		
		Assertions.assertThat(bookService.updateBookFromNto(bookUpdateNto).getResult()).isEqualTo(ServiceResult.OK);
		
		verify(authorServiceMock, times(1)).findAuthorByCf(bookUpdateNto.newBook().author().cf());
		verify(bookRepositoryMock, times(1)).save(book);
		
		when(authorServiceMock.existsByCf(book.getAuthor().getCf())).thenReturn(false);
		
		Assertions.assertThat(bookService.updateBookFromNto(bookUpdateNto).getResult()).isEqualTo(ServiceResult.SERVER_ERROR);
		
		verify(authorServiceMock, times(1)).findAuthorByCf(any());
		verify(bookRepositoryMock,times(1)).save(any());
	}
	
	@Test
	void updateBookConflict()
	{
		Book book = BookHelper.getRandomBook();
		Book bookEdit = Book.createTransientBook(BookNto.fromBook(book));
		bookEdit.update(BookHelper.getRandomBook());
		
		when(bookRepositoryMock.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of(book, book)));
		
		var result = bookService.updateBookFromNto(new BookUpdateNto(BookNto.fromBook(book), BookNto.fromBook(bookEdit)));
		Assertions.assertThat(result.getResult()).isEqualTo(ServiceResult.CONFLICT);
		assertNull(result.get());
		verify(bookRepositoryMock, never()).save(any());
	}
	
	@Test
	void updateBookNotFound()
	{
		Book book = BookHelper.getRandomBook();
		Book bookEdit = Book.createTransientBook(BookNto.fromBook(book));
		bookEdit.update(BookHelper.getRandomBook());
		
		when(bookRepositoryMock.findByTitleAndIsbn(book.getTitle(), book.getIsbn())).thenReturn(new ArrayList<>(List.of()));
		
		var result = bookService.updateBookFromNto(new BookUpdateNto(BookNto.fromBook(book), BookNto.fromBook(bookEdit)));
		Assertions.assertThat(result.getResult()).isEqualTo(ServiceResult.NOT_FOUND);
		assertNull(result.get());
		verify(bookRepositoryMock, never()).save(any());
	}
	
	private static AuthorNto getAuthor()
	{
		return new AuthorNto("g", "test", "test", LocalDate.now());
	}
}
