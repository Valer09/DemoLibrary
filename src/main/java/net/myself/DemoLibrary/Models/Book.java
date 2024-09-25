package net.myself.DemoLibrary.Models;

import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;

public class Book
{
	private final long _id;
	private final String title;
	private final String author;
	private final String isbn;
	private final LocalDate publishedDate;
	
	private Book(long _id, String title, String author, String isbn, LocalDate publishedDate)
	{
		this._id = _id;
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.publishedDate = publishedDate;
	}
	
	public boolean isEmpty()
	{
		return _id == -1;
	}
	
	public static Book createEmptyBook()
	{
		return new Book(-1, Strings.EMPTY, Strings.EMPTY, Strings.EMPTY, LocalDate.MIN);
	}
	
	public static Book createBook(long _id, String title, String author, String isbn, LocalDate publishedDate)
	{
		return new Book(_id, title, author, isbn, publishedDate);
	}
	
	public long get_id()
	{
		return _id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getAuthor()
	{
		return author;
	}
	
	public String getIsbn()
	{
		return isbn;
	}
	
	public LocalDate getPublishedDate()
	{
		return publishedDate;
	}
}
