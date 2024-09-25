package net.myself.DemoLibrary.Data.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Book
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long _id;
	
	private String title;
	private String author;
	private String isbn;
	private LocalDate publishedDate;
	
	public Book(){}
	
	public Book(long _id, String title, String author, String isbn, LocalDate publishedDate)
	{
		this._id = _id;
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.publishedDate = publishedDate;
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
