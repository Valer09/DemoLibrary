package net.myself.DemoLibrary.Data.Entities;
import jakarta.persistence.*;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Author
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false, unique = true)
	private String cf;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String surname;
	@Column()
	private LocalDate birth;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
	private List<Book> books;
	
	public Author(){}
	public Author(long id, String cf, String name, String surname, LocalDate birth, List<Book> books)
	{
		this.id = id;
		this.cf = cf;
		this.name = name;
		this.surname = surname;
		this.birth = birth;
		this.books = books;
	}
	
	public Author(String cf, String name, String surname, LocalDate birth, List<Book> books)
	{
		this.cf = cf;
		this.name = name;
		this.surname = surname;
		this.birth = birth;
		this.books = books;
	}
	
	public Author(String cf, String name, String surname, LocalDate birth)
	{
		this.cf = cf;
		this.name = name;
		this.surname = surname;
		this.birth = birth;
	}
	
	public static Author createTransientAuthor(AuthorNto authorNto)
	{
		return new Author(authorNto.cf(), authorNto.name(), authorNto.surname(), authorNto.birth());
	}
	
	public String getCf()
	{
		return cf;
	}
	
	public long getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getSurname()
	{
		return surname;
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
