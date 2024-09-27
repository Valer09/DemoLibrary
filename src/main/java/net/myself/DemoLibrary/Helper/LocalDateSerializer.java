package net.myself.DemoLibrary.Helper;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer implements JsonSerializer<LocalDate>
{
	private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
	
	@Override
	public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(date.format(formatter));
	}
}