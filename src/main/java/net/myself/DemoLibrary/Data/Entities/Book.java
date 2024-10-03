package net.myself.DemoLibrary.Data.Entities;
import jakarta.persistence.*;
import net.myself.DemoLibrary.Data.NTO.BookNto;

import java.time.LocalDate;

@Entity
public class Book
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false, unique = true)
	private String isbn;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private Author author;
	private String title;
	private LocalDate publishedDate;
	
	public Book(){}
	public Book(long id, String title, Author author, String isbn, LocalDate publishedDate)
	{
		this.id = id;
		this.setTitle(title);
		this.setAuthor(author);
		this.setIsbn(isbn);
		this.setPublishedDate(publishedDate);
	}
	private Book(String title, Author author, String isbn, LocalDate publishedDate)
	{
		this.setTitle(title);
		this.setAuthor(author);
		this.setIsbn(isbn);
		this.setPublishedDate(publishedDate);
	}
	
	public static Book createTransientBook(BookNto bookNto)
	{
		return new Book(bookNto.title(), Author.createTransientAuthor(bookNto.author()), bookNto.isbn(), bookNto.publishedDate());
	}
	
	public static Book createTransientBook(BookNto bookNto, Author author)
	{
		return new Book(bookNto.title(), author, bookNto.isbn(), bookNto.publishedDate());
	}
	
	public long getId()
	{
		return id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Author getAuthor()
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
	
	private void setAuthor(Author author)
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
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		
		if  (!(obj instanceof Book)) return false;
		
		return id >=0 && id == (((Book) obj).getId());
	}
}
