package net.myself.DemoLibrary.Helper;

import net.myself.DemoLibrary.Data.Entities.Author;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Service.ServiceResult;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class BookService_RepositoryACT
{
	public static void configureUpdateIsbnExpectations(ServiceResult serviceResult, long bookId, String isbn, String newIsbn, IBookRepository bookRepositoryMock)
	{
		if(serviceResult == ServiceResult.OK)
		{
			when(bookRepositoryMock.existsByIsbnAndDeletedFalse(isbn)).thenReturn(true);
			when(bookRepositoryMock.existsByIsbnAndDeletedFalse(newIsbn)).thenReturn(false);
			when(bookRepositoryMock.findByIsbnAndDeletedFalse(isbn)).thenReturn(Optional.of(new Book(bookId, "", new Author(), isbn, LocalDate.now())));
			when(bookRepositoryMock.updateIsbnById(bookId, newIsbn)).thenReturn(1);
		}
		else if(serviceResult == ServiceResult.NOT_FOUND)
			when(bookRepositoryMock.existsByIsbnAndDeletedFalse(isbn)).thenReturn(false);
		else if(serviceResult == ServiceResult.CONFLICT)
		{
			when(bookRepositoryMock.existsByIsbnAndDeletedFalse(isbn)).thenReturn(true);
			when(bookRepositoryMock.existsByIsbnAndDeletedFalse(newIsbn)).thenReturn(true);
		}
		else if(serviceResult == ServiceResult.SERVER_ERROR)
		{
			when(bookRepositoryMock.existsByIsbnAndDeletedFalse(isbn)).thenReturn(true);
			when(bookRepositoryMock.existsByIsbnAndDeletedFalse(newIsbn)).thenReturn(false);
			when(bookRepositoryMock.findByIsbnAndDeletedFalse(isbn)).thenReturn(Optional.of(new Book(bookId, "", new Author(), isbn, LocalDate.now())));
			when(bookRepositoryMock.updateIsbnById(bookId, newIsbn)).thenReturn(-1);
		}
	}
}
