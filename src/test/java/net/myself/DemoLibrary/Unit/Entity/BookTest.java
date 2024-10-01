package net.myself.DemoLibrary.Unit.Entity;

import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Helper.BookHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookTest
{
	@Test
	void updateTest()
	{
		Book book = BookHelper.getRandomBookWithId();
		Book other = BookHelper.getRandomBook();
		book.update(other);
		Assertions.assertEquals(book.getId(), book.getId());
		Assertions.assertEquals(book.getTitle(), other.getTitle());
		Assertions.assertEquals(book.getIsbn(), other.getIsbn());
		Assertions.assertEquals(book.getAuthor(), other.getAuthor());
		Assertions.assertEquals(book.getPublishedDate(), other.getPublishedDate());
	}
}
