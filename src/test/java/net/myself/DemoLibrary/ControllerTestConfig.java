package net.myself.DemoLibrary;

import net.myself.DemoLibrary.Cucumber.StepDefinitions.TestContextCache;
import net.myself.DemoLibrary.Helper.AuthorControllerEndPointsMap;
import net.myself.DemoLibrary.Helper.BookControllerEndPointsMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootTest
public class ControllerTestConfig
{
	
	@Value("${version.lastApiVersion}")
	private String lastApiVersion;
	
	@Bean
	public String lastApiVersionBean() {
		return lastApiVersion;
	}
	
	@Bean
	public BookControllerEndPointsMap bookControllerEndPointsMapBean() {
		return new BookControllerEndPointsMap(lastApiVersion);
	}
	
	@Bean
	public AuthorControllerEndPointsMap authorControllerEndPointsMapBean() {
		return new AuthorControllerEndPointsMap(lastApiVersion);
	}
	
	@Bean
	public TestContextCache testContextCacheBean()
	{
		return new TestContextCache();
	}
}
