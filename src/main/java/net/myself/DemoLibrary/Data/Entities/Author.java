package net.myself.DemoLibrary.Data.Entities;
import jakarta.persistence.*;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Model.AuthorUpdate;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Author
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false, unique = true)
	private String isni;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String lastName;
	@Column()
	private LocalDate birth;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
	private List<Book> books;
	
	public Author(){}
	public Author(long id, String isni, String name, String lastName, LocalDate birth, List<Book> books)
	{
		this.id = id;
		this.isni = isni;
		this.name = name;
		this.lastName = lastName;
		this.birth = birth;
		this.books = books;
	}
	
	public Author(String isni, String name, String lastName, LocalDate birth, List<Book> books)
	{
		this.isni = isni;
		this.name = name;
		this.lastName = lastName;
		this.birth = birth;
		this.books = books;
	}
	
	public Author(String isni, String name, String lastName, LocalDate birth)
	{
		this.isni = isni;
		this.name = name;
		this.lastName = lastName;
		this.birth = birth;
	}
	
	public static Author createTransientAuthor(AuthorNto authorNto)
	{
		return new Author(authorNto.isni(), authorNto.name(), authorNto.lastName(), authorNto.birth());
	}
	
	public String getIsni()
	{
		return isni;
	}
	
	public long getId()
	{
		return id;
	}
	
	public void update(AuthorUpdate authorUpdate)
	{
		this.name = authorUpdate.name();
		this.lastName = authorUpdate.lastName();
		this.birth = authorUpdate.birth();
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getFullName()
	{
		return name+" "+ lastName;
	}
	
	public String getLastname()
	{
		return lastName;
	}
	
	public LocalDate getBirth()
	{
		return birth;
	}
	
	public List<Book> getBooks()
	{
		return books;
	}
}
