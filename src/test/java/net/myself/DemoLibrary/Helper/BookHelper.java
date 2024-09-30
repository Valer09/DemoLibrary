package net.myself.DemoLibrary.Helper;

import com.github.javafaker.Faker;
import net.myself.DemoLibrary.Data.Entities.Book;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BookHelper
{
	public static Book getRandomBook()
	{
		Faker faker = new Faker();
		Date d = faker.date().past(1, TimeUnit.DAYS);
		Instant instant = d.toInstant();
		return new Book(new Random().nextInt(100), faker.book().title(), faker.book().author(), faker.code().isbn10(), instant.atZone(ZoneId.systemDefault()).toLocalDate());
	}
}
