package net.myself.DemoLibrary.Cucumber.StepDefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import net.myself.DemoLibrary.BookControllerTestConfig;
import net.myself.DemoLibrary.Cucumber.Configuration.CucumberSpringConfiguration;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Helper.AuthorControllerEndPointsMap;
import net.myself.DemoLibrary.Service.AuthorService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Import(BookControllerTestConfig.class)
public class AuthorControllerStepDefinition extends CucumberSpringConfiguration
{
	@MockBean
	private AuthorService authorService;
	@Autowired
	private AuthorControllerEndPointsMap authorControllerEndPointsMap;
	@Autowired
	private ObjectMapper jackson;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	TestContextCache testContextCache;
	private AuthorNto authorNto;
	ResultActions resultAction;
	
	@Before
	public void initialize()
	{
		testContextCache.reset();
	}
	
	@Given("the following Author nto:")
	public void anAuthorNto(AuthorNto authorNto)
	{
		this.authorNto = authorNto;
	}
	
	@Given("the author service is set to simulate {stringParam}")
	public void theAuthorServiceIsSetToSimulate (String serviceOutcome) throws Exception
	{
		var serialized = jackson.writeValueAsString(authorNto);
		var transientAuthorNto = jackson.readValue(serialized, AuthorNto.class);
		var serviceResult = EntityTransformer.getServiceResponse(serviceOutcome, authorNto, "");
		when(authorService.addAuthorNto(transientAuthorNto)).thenReturn(serviceResult);
		
		testContextCache.setProperty("serialized_author", serialized);
		testContextCache.setProperty("transientAuthorNto", transientAuthorNto);
	}
	
	@When("the client makes a request to add the author providing the nto")
	public void theClientMakeARequestToAddTheAuthorProvidingAnAuthorNto() throws Exception
	{
		String serialized = (String)(testContextCache.getProperty("serialized_author"));
		resultAction = mockMvc.perform(authorControllerEndPointsMap.addBook(serialized));
	}
	
	@Then("the api responses is {int}")
	public void theApiResponsesOk(int apiResponse) throws Exception
	{
		resultAction.andExpect(status().is(apiResponse));
	}
	
	@Then("the service add operation has been called")
	public void theServiceAddOperationHasBeenCalled()
	{
		var transientAuthorNto = (AuthorNto)testContextCache.getProperty("transientAuthorNto");
		Mockito.verify(authorService, times(1)).addAuthorNto(transientAuthorNto);
	}
}
