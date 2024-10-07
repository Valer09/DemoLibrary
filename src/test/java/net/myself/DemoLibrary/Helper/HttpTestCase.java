package net.myself.DemoLibrary.Helper;

import net.myself.DemoLibrary.Unit.Controller.BookControllerTest;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

public class HttpTestCase
{
	public String description;
	public HttpStatus expectedStatus;
	
	HttpTestCase(String description, HttpStatus expectedStatus)
	{
		this.description = description;
		this.expectedStatus = expectedStatus;
	}
	
	public static Stream<HttpTestCase> addBookTestCases()
	{
		return Stream.of(
						new HttpTestCase("Book added", HttpStatus.CREATED),
						new HttpTestCase("Conflict when book exists", HttpStatus.CONFLICT),
						new HttpTestCase("Internal server error when author not found", HttpStatus.INTERNAL_SERVER_ERROR));
	}
	public static Stream<HttpTestCase> deleteBookByIsbn()
	{
			return Stream.of(
							new HttpTestCase("Book deleted successfully", HttpStatus.OK),
							new HttpTestCase("Book not found", HttpStatus.NOT_FOUND)
			);
	}
	
	public static Stream<HttpTestCase> updateBook()
	{
		return Stream.of(
						new HttpTestCase("Book updated successfully", HttpStatus.OK),
						new HttpTestCase("Book not found", HttpStatus.NOT_FOUND),
						new HttpTestCase("Author not found", HttpStatus.INTERNAL_SERVER_ERROR),
						new HttpTestCase("Internal Server error", HttpStatus.INTERNAL_SERVER_ERROR)
		);
	}
	
	public static Stream<HttpTestCase> updateIsbn()
	{
		return Stream.of(
						new HttpTestCase("1", HttpStatus.OK),
						new HttpTestCase("Book not found", HttpStatus.NOT_FOUND),
						new HttpTestCase("Isbn already existing", HttpStatus.CONFLICT),
						new HttpTestCase("Internal Server error", HttpStatus.INTERNAL_SERVER_ERROR)
		);
	}
}
