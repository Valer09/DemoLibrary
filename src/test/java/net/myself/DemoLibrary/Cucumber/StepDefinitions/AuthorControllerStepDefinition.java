package net.myself.DemoLibrary.Cucumber.StepDefinitions;

import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
	
	@Given("the author service is set to simulate {stringParam} on update isni with {stringParam}")
	public void theAuthorServiceIsSetToSimulateOnUpdateIsni (String serviceOutcome, String newIsni) throws Exception
	{
		
		var serviceResult = EntityTransformer.getServiceResponse(serviceOutcome, 1, "");
		when(authorService.updateIsni(authorNto.isni(), newIsni)).thenReturn(serviceResult);
		
		testContextCache.setProperty("newIsni", newIsni);
	}
	
	@Given("the author service is set to simulate {stringParam} on find by isni")
	public void theAuthorServiceIsSetToSimulateOnFindByIsni (String serviceOutcome) throws Exception
	{
		var serviceResult = EntityTransformer.getServiceResponse(serviceOutcome, authorNto, "");
		when(authorService.findAuthorByIsniNto(authorNto.isni())).thenReturn(serviceResult);
	}
	
	@Given("the following list of authors in the system:")
	public void theFollowingListOfAuthorsInTheSystem (List<AuthorNto> authorList) throws Exception
	{
		testContextCache.setProperty("authorList", authorList);
	}
	
	@Given("the author service is set to simulate {stringParam} on find by name")
	public void theAuthorServiceIsSetToSimulateOnFindByName (String serviceOutcome) throws Exception
	{
		var serviceResponse = EntityTransformer.getServiceResponse(serviceOutcome, testContextCache.getProperty("authorList"), "");
		
		when(authorService.findByNameContainingIgnoreCaseNto(any())).thenReturn((List<AuthorNto>) serviceResponse.get());
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
	
	@When("the client makes a request find the author by name {stringParam}")
	public void theClientMakesARequestFindAuthorByName(String name) throws Exception
	{
		resultAction = mockMvc.perform(authorControllerEndPointsMap.searchByName(name));
		testContextCache.setProperty("searchName", name);
	}
	
	@When("the client makes a request update isni with {stringParam}")
	public void theClientMakesARequestUpdateIsniWith(String newIsni) throws Exception
	{
		resultAction = mockMvc.perform(authorControllerEndPointsMap.updateIsni(authorNto.isni(), newIsni));
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
	
	@Then("the service find by name operation has been called")
	public void theServiceFindByNameOperationHasBeenCalled()
	{
		Mockito.verify(authorService, times(1)).findByNameContainingIgnoreCaseNto((String)testContextCache.getProperty("searchName"));
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
	
	@Then("the response contains the list of authors")
	public void theResponseContainsTheListOfAuthors () throws Exception
	{
		var resultAuthor = jackson.readValue(resultAction.andReturn().getResponse().getContentAsString(),  new TypeReference<List<AuthorNto>>() {});
		
		Assertions.assertEquals(resultAuthor.size(), 4);
		
		List<AuthorNto> authorList = (List<AuthorNto>) testContextCache.getProperty("authorList");
		
		for(int i = 0; i < resultAuthor.size(); i++)
		{
			AuthorNto fromResponse = resultAuthor.get(i);
			AuthorNto fromStored = authorList.get(i);
			Assertions.assertEquals(fromResponse.isni(), fromStored.isni());
			Assertions.assertEquals(fromResponse.name(), fromStored.name());
			Assertions.assertEquals(fromResponse.lastName(), fromStored.lastName());
		}
	}
	
	@Then("the service update isni operation has been called")
	public void theServiceUpdateIsniOperationHasBeenCalled () throws Exception
	{
		verify(authorService, times(1)).updateIsni(authorNto.isni(), (String)testContextCache.getProperty("newIsni"));
	}
	
	@Then("the response contains the value {int}")
	public void theResponseContainsTheValue (int value) throws Exception
	{
		if(value == 0) Assertions.assertTrue(resultAction.andReturn().getResponse().getContentAsString().isEmpty());
		int resultValue = Integer.parseInt(resultAction.andReturn().getResponse().getContentAsString());
		Assertions.assertEquals(resultValue, value);
	}
	@Then("the response contains the value null")
	public void theResponseContainsTheValueNull () throws Exception
	{
		Assertions.assertTrue(resultAction.andReturn().getResponse().getContentAsString().isEmpty());
	}
}
