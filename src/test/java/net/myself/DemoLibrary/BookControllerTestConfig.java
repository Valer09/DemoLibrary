package net.myself.DemoLibrary;

import net.myself.DemoLibrary.Helper.BookControllerEndPointsMap;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class BookControllerTestConfig
{
	@Bean
	public BookControllerEndPointsMap bookControllerEndPointsMap() {
		return new BookControllerEndPointsMap();
	}
}
