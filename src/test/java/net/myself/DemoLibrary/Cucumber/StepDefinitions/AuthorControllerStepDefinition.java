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
import org.junit.jupiter.api.Assertions;
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
	
	@Given("the author service is set to simulate {stringParam} on add author")
	public void theAuthorServiceIsSetToSimulate (String serviceOutcome) throws Exception
	{
		var serialized = jackson.writeValueAsString(authorNto);
		var transientAuthorNto = jackson.readValue(serialized, AuthorNto.class);
		var serviceResult = EntityTransformer.getServiceResponse(serviceOutcome, authorNto, "");
		when(authorService.addAuthorNto(transientAuthorNto)).thenReturn(serviceResult);
		
		testContextCache.setProperty("serialized_author", serialized);
		testContextCache.setProperty("transientAuthorNto", transientAuthorNto);
	}
	
	@Given("the author service is set to simulate {stringParam} on find by isni")
	public void theAuthorServiceIsSetToSimulateOnFindByIsni (String serviceOutcome) throws Exception
	{
		var serviceResult = EntityTransformer.getServiceResponse(serviceOutcome, authorNto, "");
		when(authorService.findAuthorByIsniNto(authorNto.isni())).thenReturn(serviceResult);
	}
	
	@When("the client makes a request to add the author providing the nto")
	public void theClientMakeARequestToAddTheAuthorProvidingAnAuthorNto() throws Exception
	{
		String serialized = (String)(testContextCache.getProperty("serialized_author"));
		resultAction = mockMvc.perform(authorControllerEndPointsMap.addBook(serialized));
	}
	
	@When("the client makes a request find the author by isni {stringParam}")
	public void theClientMakesARequestFindTheAuthorByIsni(String isni) throws Exception
	{
		resultAction = mockMvc.perform(authorControllerEndPointsMap.finByIsni(isni));
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
	
	@Then("the service find by isni operation has been called")
	public void theServiceFindByIsniOperationHasBeenCalled()
	{
		Mockito.verify(authorService, times(1)).findAuthorByIsniNto(authorNto.isni());
	}
	
	@Then("the response contains the author with the isni {stringParam}")
	public void theResponseContainsTheAuthorWithTheIsni(String isni) throws Exception
	{
		if(isni.equals("empty")) Assertions.assertTrue(resultAction.andReturn().getResponse().getContentAsString().isEmpty());
		else
		{
			var resultAuthor = jackson.readValue(resultAction.andReturn().getResponse().getContentAsString(), AuthorNto.class);
			Assertions.assertEquals(resultAuthor.isni(), isni);
			Assertions.assertEquals(resultAuthor.name(), authorNto.name());
			Assertions.assertEquals(resultAuthor.lastName(), authorNto.lastName());
			Assertions.assertEquals(resultAuthor.birth(), authorNto.birth());
			Assertions.assertEquals(resultAuthor.isni(), authorNto.isni());
		}
	}
}
