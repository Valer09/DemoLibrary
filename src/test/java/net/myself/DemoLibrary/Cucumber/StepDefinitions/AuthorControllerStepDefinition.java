package net.myself.DemoLibrary.Cucumber.StepDefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import net.myself.DemoLibrary.BookControllerTestConfig;
import net.myself.DemoLibrary.Cucumber.Configuration.CucumberSpringConfiguration;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Helper.AuthorControllerEndPointsMap;
import net.myself.DemoLibrary.Helper.BookHelper;
import net.myself.DemoLibrary.Service.AuthorService;
import net.myself.DemoLibrary.Service.ServiceResponse;
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
	private AuthorNto authorNto;
	private AuthorNto transientAuthorNto;
	ResultActions resultAction;
	
	@Given("an author NTO")
	public void anAuthorNto()
	{
		authorNto = AuthorNto.fromAuthor(BookHelper.getRandomAuthor());
	}
	
	@When("the client makes a request to add an author providing an author NTO")
	public void theClientMakeARequestToAddAnAuthorProvidingAnAuthorNto() throws Exception
	{
		var serialized = jackson.writeValueAsString(authorNto);
		transientAuthorNto = jackson.readValue(serialized, AuthorNto.class);
		
		when(authorService.addAuthorNto(transientAuthorNto)).thenReturn(ServiceResponse.createOk(authorNto));
		
		resultAction = mockMvc.perform(authorControllerEndPointsMap.addBook(serialized));
	}
	
	@Then("the api responses ok")
	public void theApiResponsesOk() throws Exception
	{
		resultAction.andExpect(status().isCreated());
	}
	
	@Then("the service add operation has been called")
	public void theServiceAddOperationHasBeenCalled()
	{
		Mockito.verify(authorService, times(1)).addAuthorNto(transientAuthorNto);
	}
}
