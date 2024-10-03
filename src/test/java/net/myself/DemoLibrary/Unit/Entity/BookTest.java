package net.myself.DemoLibrary.Unit.Entity;

import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Helper.BookHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class BookTest
{
	@Test
	void updateTest()
	{
		Book book = BookHelper.getRandomBookWithId();
		var id = book.getId();
		Book other = BookHelper.getRandomBook();
		book.update(other);
		Assertions.assertEquals(id, book.getId());
		Assertions.assertEquals(book.getTitle(), other.getTitle());
		Assertions.assertEquals(book.getIsbn(), other.getIsbn());
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
		second.update(BookHelper.getRandomBook());
		Assertions.assertEquals(first, second);
		Assertions.assertEquals(first, new Book(first.getId(), "",BookHelper.getRandomAuthor(),"", LocalDate.now()));
	}
}
