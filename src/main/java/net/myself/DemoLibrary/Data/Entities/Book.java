package net.myself.DemoLibrary.Data.Entities;
import jakarta.persistence.*;
import net.myself.DemoLibrary.Model.BookUpdate;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import org.hibernate.annotations.Formula;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class Book implements IBook
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
	
	@Formula("(SELECT CASE WHEN COUNT(br.id) > 0 THEN 'Not available' ELSE 'Available' END " +
					"FROM book_rental br WHERE br.book_id = id AND br.state = 'RENTED')")
	private String state;
	
	
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
		return new Book(bookNto.title(), Author.createTransientAuthor(bookNto.authorNto()), bookNto.isbn(), bookNto.publishedDate());
	}
	
	public static Book createTransientBook(BookNto bookNto, Author author)
	{
		return new Book(bookNto.title(), author, bookNto.isbn(), bookNto.publishedDate());
	}
	
	@Override
	public long getId()
	{
		return id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	@Override
	public Author getAuthor()
	{
		return author;
	}
	
	@Override
	public String getIsbn()
	{
		return isbn;
	}
	
	@Override
	public LocalDate getPublishedDate()
	{
		return publishedDate;
	}
	@Override
	public String getState()
	{
		return this.state;
	}
	
	public DeletedBook getTransientDeletedBook()
	{
		return DeletedBook.getTransientDeletedBook(this);
	}
	
	@Override
	public void update(BookUpdate newBook)
	{
		setTitle(newBook.title());
		setAuthor(newBook.author());
		setPublishedDate(newBook.publishedDate());
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
		
		return (id >=0 && id == (((Book) obj).getId()) && isbn.equals(((Book) obj).getIsbn()));
	}
}
