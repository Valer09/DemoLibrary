package net.myself.DemoLibrary.Data.Entities;

import jakarta.persistence.*;
import net.myself.DemoLibrary.Model.BookUpdate;

import java.time.LocalDate;

@Entity
public class DeletedBook implements IBook
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false, unique = true)
	private String isbn;
	private String title;
	private LocalDate publishedDate;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private Author author;
	
	private final String state = "DELETED";
	
	public DeletedBook(){}
	public DeletedBook(long id, String title, Author author, String isbn, LocalDate publishedDate)
	{
		this.id = id;
		this.setTitle(title);
		this.setAuthor(author);
		this.setIsbn(isbn);
		this.setPublishedDate(publishedDate);
	}
	
	private DeletedBook(String title, Author author, String isbn, LocalDate publishedDate)
	{
		this.setTitle(title);
		this.setAuthor(author);
		this.setIsbn(isbn);
		this.setPublishedDate(publishedDate);
	}
	
	public static DeletedBook getTransientDeletedBook(Book book)
	{
		return new DeletedBook(book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublishedDate());
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
	public long getId()
	{
		return id;
	}
	
	@Override
	public Author getAuthor()
	{
		return this.author;
	}
	
	@Override
	public String getIsbn()
	{
		return this.isbn;
	}
	
	@Override
	public LocalDate getPublishedDate()
	{
		return getPublishedDate();
	}
	
	@Override
	public String getState()
	{
		return this.state;
	}
	
	@Override
	public String getTitle()
	{
		return this.title;
	}
	@Override
	public void update(BookUpdate newBook)
	{
	
	}
	
}


