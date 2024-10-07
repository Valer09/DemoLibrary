package net.myself.DemoLibrary.Helper;

import net.myself.DemoLibrary.Data.Entities.Author;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Service.BookService;
import net.myself.DemoLibrary.Service.ServiceResponse;
import net.myself.DemoLibrary.Service.ServiceResult;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class BookServiceRepositoryExpectations
{
	public static void configureUpdateIsbnServiceExpectations(ServiceResult serviceResult, long bookId, String isbn, String newIsbn, IBookRepository bookRepositoryMock)
	{
		if(serviceResult == ServiceResult.OK)
		{
			when(bookRepositoryMock.existsByIsbn(isbn)).thenReturn(true);
			when(bookRepositoryMock.existsByIsbn(newIsbn)).thenReturn(false);
			when(bookRepositoryMock.findByIsbn(isbn)).thenReturn(Optional.of(new Book(bookId, "", new Author(), isbn, LocalDate.now())));
			when(bookRepositoryMock.updateIsbnById(bookId, newIsbn)).thenReturn(1);
		}
		else if(serviceResult == ServiceResult.NOT_FOUND)
			when(bookRepositoryMock.existsByIsbn(isbn)).thenReturn(false);
		else if(serviceResult == ServiceResult.CONFLICT)
		{
			when(bookRepositoryMock.existsByIsbn(isbn)).thenReturn(true);
			when(bookRepositoryMock.existsByIsbn(newIsbn)).thenReturn(true);
		}
		else if(serviceResult == ServiceResult.SERVER_ERROR)
		{
			when(bookRepositoryMock.existsByIsbn(isbn)).thenReturn(true);
			when(bookRepositoryMock.existsByIsbn(newIsbn)).thenReturn(false);
			when(bookRepositoryMock.findByIsbn(isbn)).thenReturn(Optional.of(new Book(bookId, "", new Author(), isbn, LocalDate.now())));
			when(bookRepositoryMock.updateIsbnById(bookId, newIsbn)).thenReturn(-1);
		}
	}
}
