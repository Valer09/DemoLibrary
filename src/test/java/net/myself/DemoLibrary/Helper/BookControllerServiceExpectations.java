package net.myself.DemoLibrary.Helper;

import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Service.BookService;
import net.myself.DemoLibrary.Service.ServiceResponse;
import net.myself.DemoLibrary.Service.ServiceResult;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

public class BookControllerServiceExpectations
{
	public static void configureAddBookServiceExpectations(HttpTestCase testCase, BookNto bookNto, BookNto deserialized, BookService bookService)
	{
		if(testCase.expectedStatus == HttpStatus.CONFLICT)
			when(bookService.addBookFromNto(deserialized)).thenReturn(ServiceResponse.createError(ServiceResult.CONFLICT, "book already exists"));
		else if(testCase.expectedStatus == HttpStatus.INTERNAL_SERVER_ERROR)
			when(bookService.addBookFromNto(deserialized)).thenReturn(ServiceResponse.createError(ServiceResult.SERVER_ERROR, "author not found"));
		else
			when(bookService.addBookFromNto(deserialized)).thenReturn(ServiceResponse.createOk(bookNto));
	}
	
	public static void configureDeleteByIsbnServiceExpectations(HttpTestCase testCase, String isbn, BookService bookService)
	{
		if(testCase.expectedStatus == HttpStatus.NOT_FOUND)
			when(bookService.deleteBookByIsbn(isbn)).thenReturn(ServiceResponse.createError(ServiceResult.NOT_FOUND, ""));
		else
			when(bookService.deleteBookByIsbn(isbn)).thenReturn(ServiceResponse.createOk(""));
	}
	
	public static void configureUpdateBookServiceExpectations(HttpTestCase testCase, BookUpdateNto nto, BookService bookService, BookNto newBook)
	{
		if(testCase.expectedStatus == HttpStatus.NOT_FOUND)
			when(bookService.updateBookFromNto(nto)).thenReturn(ServiceResponse.createError(ServiceResult.NOT_FOUND, ""));
		else if(testCase.expectedStatus == HttpStatus.INTERNAL_SERVER_ERROR)
			when(bookService.updateBookFromNto(nto)).thenReturn(ServiceResponse.createError(ServiceResult.SERVER_ERROR, ""));
		else
			when(bookService.updateBookFromNto(nto)).thenReturn(ServiceResponse.createOk(newBook));
	}
	
	public static void configureUpdateIsbnServiceExpectations(HttpTestCase testCase, String isbn, String newIsbn, BookService bookService)
	{
		if(testCase.expectedStatus == HttpStatus.OK)
			when(bookService.updateIsbn(isbn, newIsbn)).thenReturn(ServiceResponse.createOk(1));
		else if(testCase.expectedStatus == HttpStatus.CONFLICT)
			when(bookService.updateIsbn(isbn, newIsbn)).thenReturn(ServiceResponse.createError(ServiceResult.CONFLICT, "isbn already existing"));
		else if(testCase.expectedStatus == HttpStatus.NOT_FOUND)
			when(bookService.updateIsbn(isbn, newIsbn)).thenReturn(ServiceResponse.createError(ServiceResult.NOT_FOUND, "book not found"));
		else
			when(bookService.updateIsbn(isbn, newIsbn)).thenReturn(ServiceResponse.createError(ServiceResult.SERVER_ERROR, "internal server error"));
	}
}
