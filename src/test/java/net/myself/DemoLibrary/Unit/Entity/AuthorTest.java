package net.myself.DemoLibrary.Unit.Entity;

import net.myself.DemoLibrary.Data.Entities.Author;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Helper.BookHelper;
import net.myself.DemoLibrary.Model.AuthorUpdate;
import net.myself.DemoLibrary.Model.BookUpdate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class AuthorTest
{
	@Test
	void updateTest()
	{
		Author author = BookHelper.getRandomAuthor();
		var id = author.getId();
		var isni = author.getIsni();
		Author other = BookHelper.getRandomAuthor();
		author.update(new AuthorUpdate(other.getName(), other.getLastname(), other.getBirth()));
		Assertions.assertEquals(id, author.getId());
		Assertions.assertEquals(author.getName(), other.getName());
		Assertions.assertEquals(author.getIsni(), isni);
		Assertions.assertEquals(author.getLastname(), other.getLastname());
		Assertions.assertEquals(author.getBirth(), other.getBirth());
		Assertions.assertEquals(author.getFullName(), other.getFullName());
	}
	
	@Test
	void getFullNameTest()
	{
		Author author = BookHelper.getRandomAuthor();
		Assertions.assertEquals(author.getFullName(), author.getName()+ " " + author.getLastname());
	}
	
	@Test
	void equalsTest()
	{
		Author author = BookHelper.getRandomAuthor();
		Author first = new Author(author.getIsni(), author.getName(), author.getLastname(), author.getBirth(), new ArrayList<>());
		Author second = new Author(author.getIsni(), author.getName(), author.getLastname(), author.getBirth(), new ArrayList<>());
		Assertions.assertEquals(author, author);
		Assertions.assertEquals(first, second);
		second.update(new AuthorUpdate("test", "test2", LocalDate.now().minus(4, ChronoUnit.YEARS)));
		Assertions.assertEquals(first, second);
		
		Assertions.assertFalse(first.equals(new Author("isni", first.getName(), first.getLastname(),first.getBirth(), new ArrayList<>())));
	}
}
