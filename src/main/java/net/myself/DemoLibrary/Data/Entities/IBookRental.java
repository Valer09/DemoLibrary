package net.myself.DemoLibrary.Data.Entities;

import java.time.LocalDate;
import java.util.UUID;

public interface IBookRental
{
	Long getId();
	
	String getUserId();
	
	LocalDate getStartingDate();
	
	LocalDate getEndingDate();
	
	String getState();
	
	UUID getRentalCode();
	
	String getBookIsbn();
	
	String getBookTitle();
	
	String getAuthorName();
}
