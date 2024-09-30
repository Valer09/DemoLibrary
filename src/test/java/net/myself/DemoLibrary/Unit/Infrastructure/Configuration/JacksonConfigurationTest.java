package net.myself.DemoLibrary.Unit.Infrastructure.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class JacksonConfigurationTest
{
	@Autowired
	private ObjectMapper jackson;
	@Test
	void checkJacksonConfiguration()
	{
		assertFalse(jackson.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
	}
}
