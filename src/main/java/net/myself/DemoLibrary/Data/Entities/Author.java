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
	private String lastname;
	@Column()
	private LocalDate birth;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
	private List<Book> books;
	
	public Author(){}
	
	public Author(String isni, String name, String lastname, LocalDate birth, List<Book> books)
	{
		this.isni = isni;
		this.name = name;
		this.lastname = lastname;
		this.birth = birth;
		this.books = books;
	}
	
	public Author(String isni, String name, String lastname, LocalDate birth)
	{
		this.isni = isni;
		this.name = name;
		this.lastname = lastname;
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
		this.lastname = authorUpdate.lastName();
		this.birth = authorUpdate.birth();
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getFullName()
	{
		return name+" "+ lastname;
	}
	
	public String getLastname()
	{
		return lastname;
	}
	
	public LocalDate getBirth()
	{
		return birth;
	}
	
	public List<Book> getBooks()
	{
		return books;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		
		if  (!(obj instanceof Author)) return false;
		
		return (id >=0 && id == (((Author) obj).getId()) && isni.equals((((Author) obj).getIsni())));
	}
}
