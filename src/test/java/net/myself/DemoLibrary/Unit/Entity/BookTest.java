package net.myself.DemoLibrary.Unit.Entity;

import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Helper.BookHelper;
import net.myself.DemoLibrary.Model.BookUpdate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookTest
{
	@Test
	void updateTest()
	{
		Book book = BookHelper.getRandomBookWithId();
		var id = book.getId();
		var isbn = book.getIsbn();
		Book other = BookHelper.getRandomBook();
		book.update(new BookUpdate(other.getTitle(), other.getAuthor(), other.getPublishedDate()));
		Assertions.assertEquals(id, book.getId());
		Assertions.assertEquals(book.getTitle(), other.getTitle());
		Assertions.assertEquals(book.getIsbn(), isbn);
		Assertions.assertEquals(book.getAuthor(), other.getAuthor());
		Assertions.assertEquals(book.getPublishedDate(), other.getPublishedDate());
	}
	
	@Test
	void equalsTest()
	{
		Book book = BookHelper.getRandomBookWithId();
		Book first = new Book(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublishedDate());
		Book second = new Book(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublishedDate());
		Assertions.assertEquals(book, book);
		Assertions.assertEquals(first, second);
		second.update(new BookUpdate("test", BookHelper.getRandomAuthor(), LocalDate.now().minus(4, ChronoUnit.YEARS)));
		Assertions.assertEquals(first, second);
		Assertions.assertEquals(first, new Book(first.getId(), "",BookHelper.getRandomAuthor(),book.getIsbn(), LocalDate.now()));
		Assertions.assertFalse(first.equals(new Book(first.getId(), "",BookHelper.getRandomAuthor(),"", LocalDate.now())));
		Assertions.assertFalse(first.equals(new Book(first.getId()+1, "",BookHelper.getRandomAuthor(),book.getIsbn(), LocalDate.now())));
	}
}
