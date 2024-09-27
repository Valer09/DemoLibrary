package net.myself.DemoLibrary.Data.Entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Book
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false, unique = true)
	private String isbn;
	private String title;
	private String author;
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
	
	public int hashCode() {
		return getClass().hashCode();
	}
	
	public boolean equals(Object obj) {
		if (this == obj) return true;
		
		if  ( obj == null || getClass() != obj.getClass() ) return false;
		Book other = (Book) obj;
		
		return id >=0 &&
						id == (other.getId());
	}
}
