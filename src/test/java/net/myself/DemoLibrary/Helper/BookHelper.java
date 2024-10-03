package net.myself.DemoLibrary.Helper;

import com.github.javafaker.Faker;
import net.myself.DemoLibrary.Data.Entities.Author;
import net.myself.DemoLibrary.Data.Entities.Book;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class BookHelper
{
	public static Book getRandomBook()
	{
		Faker faker = new Faker();
		return new Book(1, faker.book().title(), getAuthor(), faker.code().isbn10(), getDate(faker, 1));
	}
	
	public static Book getRandomBookWithId()
	{
		Faker faker = new Faker();
		return new Book(new Random().nextInt(100), faker.book().title(), getAuthor(), faker.code().isbn10(), getDate(faker, 1));
	}
	
	public static Author getRandomAuthor()
	{
		long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
		long maxDay = LocalDate.of(2015, 12, 31).toEpochDay();
		long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
		LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
		
		Faker faker = new Faker();
    return new Author(faker.idNumber().ssnValid(), faker.name().firstName(), faker.name().lastName(), randomDate, new ArrayList<>(Arrays.asList(getRandomBook(), getRandomBook(), getRandomBook())));
	}
	
	private static Author getAuthor()
	{
		Faker faker = new Faker();
		return new Author(faker.idNumber().ssnValid(), faker.name().firstName(), faker.name().lastName(), getDate(faker, 1), new ArrayList<>());
	}
	
	private static LocalDate getDate(Faker faker, int atMost)
	{
		Date d = faker.date().past(atMost, TimeUnit.DAYS);
		Instant instant = d.toInstant();
		return instant.atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
