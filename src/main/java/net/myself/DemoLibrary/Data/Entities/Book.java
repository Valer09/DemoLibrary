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
	private long id;
	
	private String title;
	private String author;
	private String isbn;
	private LocalDate publishedDate;
	
	public Book(){}
	
	public Book(long id, String title, String author, String isbn, LocalDate publishedDate)
	{
		this.id = id;
		this.setTitle(title);
		this.setAuthor(author);
		this.setIsbn(isbn);
		this.setPublishedDate(publishedDate);
	}
	
	public long getId()
	{
		return id;
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
	
	public void update(Book newBook)
	{
		setTitle(newBook.getTitle());
		setAuthor(newBook.getAuthor());
		setIsbn(newBook.getIsbn());
		setPublishedDate(newBook.getPublishedDate());
	}
	
	private void setTitle(String title)
	{
		this.title = title;
	}
	
	private void setAuthor(String author)
	{
		this.author = author;
	}
	
	private void setIsbn(String isbn)
	{
		this.isbn = isbn;
	}
	
	private void setPublishedDate(LocalDate publishedDate)
	{
		this.publishedDate = publishedDate;
	}
}
