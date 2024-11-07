package net.myself.DemoLibrary.Data.Entities;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class BookRental
{
	public static final String RENTED = "RENTED";
	public static final String COMPLETED = "COMPLETED";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@UuidGenerator
	private UUID rentalCode;
	
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "book_isbn", referencedColumnName = "isbn", nullable = false)
	private Book book;
	
	@Column(name = "starting_date", nullable = false)
	private LocalDate startingDate;
	
	@Column(name = "ending_date")
	private LocalDate endingDate;
	
	@Column(name = "state", nullable = false)
	private String state;
	
	public BookRental(Long id, UUID rentalCode, String userId, Book book, LocalDate startingDate, LocalDate endingDate, String state)
	{
		this.id = id;
		this.rentalCode = rentalCode;
		this.userId = userId;
		this.book = book;
		this.startingDate = startingDate;
		this.endingDate = endingDate;
		this.state = state;
	}
	
	private BookRental(String userId, Book book, LocalDate startingDate, String state)
	{
		this.userId = userId;
		this.book = book;
		this.startingDate = startingDate;
		this.state = state;
	}
	
	public BookRental()
	{
	}
	
	public static BookRental createTransientForNewRental(String userId, Book book)
	{
		return new BookRental(userId, book, LocalDate.now(), RENTED);
	}
	
	public Long getId()
	{
		return id;
	}
	
	public String getUserId()
	{
		return userId;
	}
	
	public Book getBook()
	{
		return book;
	}
	
	public void completeRenting()
	{
		this.endingDate = LocalDate.now();
		this.state = COMPLETED;
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
}
