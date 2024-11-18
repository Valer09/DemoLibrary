package net.myself.DemoLibrary.Data.Entities;

import net.myself.DemoLibrary.Model.BookUpdate;

import java.time.LocalDate;

public interface IBook
{
	long getId();
	
	Author getAuthor();
	
	String getIsbn();
	
	LocalDate getPublishedDate();
	
	String getState();
	
	String getTitle();
	
	void update(BookUpdate newBook);
}
