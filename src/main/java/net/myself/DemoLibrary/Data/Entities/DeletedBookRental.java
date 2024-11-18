package net.myself.DemoLibrary.Data.Entities;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class DeletedBookRental implements IBookRental
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@UuidGenerator
	private UUID rentalCode;
	
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private DeletedBook book;
	
	@Column(name = "starting_date", nullable = false)
	private LocalDate startingDate;
	
	@Column(name = "ending_date")
	private LocalDate endingDate;
	
	@Column(name = "state", nullable = false)
	private String state;
	
	public DeletedBookRental(Long id, UUID rentalCode, String userId, DeletedBook book, LocalDate startingDate, LocalDate endingDate, String state)
	{
		this.id = id;
		this.rentalCode = rentalCode;
		this.userId = userId;
		this.book = book;
		this.startingDate = startingDate;
		this.endingDate = endingDate;
		this.state = state;
	}
	
	public DeletedBookRental(UUID rentalCode,String userId, DeletedBook book, LocalDate startingDate, String state)
	{
		this.userId = userId;
		this.book = book;
		this.startingDate = startingDate;
		this.state = state;
	}
	
	public DeletedBookRental()
	{
	}
	
	public Long getId()
	{
		return id;
	}
	
	public String getUserId()
	{
		return userId;
	}
	
	public DeletedBook getBook()
	{
		return book;
	}

	public LocalDate getStartingDate()
	{
		return startingDate;
	}
	
	public LocalDate getEndingDate()
	{
		return endingDate;
	}
	
	public String getState()
	{
		return state;
	}
	
	public UUID getRentalCode()
	{
		return rentalCode;
	}
	
	
	@Override
	public String getBookIsbn()
	{
		return this.getBook().getIsbn();
	}
	
	@Override
	public String getBookTitle()
	{
		return this.getBook().getTitle();
	}
	
	@Override
	public String getAuthorName()
	{
		return this.getBook().getAuthor().getFullName();
	}
	
}
