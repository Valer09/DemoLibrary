package net.myself.DemoLibrary.Unit.Infrastructure;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Unit.Controller.TestController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestController.class)
@ExtendWith(SpringExtension.class)
public class GlobalControllerExceptionHandlerTest
{
	@MockBean
	private IBookRepository bookRepository;
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void handleEntityNotFoundTest() throws Exception
	{
		var response = mockMvc.perform(get("/test/entity-not-found")).andExpect(status().isNotFound()).andReturn();
		Assertions.assertThat(response.getResponse().getContentAsString()).isEqualTo("Not found");
	}
	
	@Test
	void handleDataIntegrityViolationTest() throws Exception
	{
		var response = mockMvc.perform(get("/test/data-integrity-violation")).andExpect(status().isConflict()).andReturn();
		Assertions.assertThat(response.getResponse().getContentAsString()).isEqualTo("Data Integrity Violation");
	}
	
	@Test
	void MethodArgumentNotValidExceptionTest() throws Exception
	{
		mockMvc.perform(get("/test/MethodArgumentNotValidException").param("param", "four")).andExpect(status().isBadRequest());
	}
	
	@Test
	void HandlerMethodValidationExceptionTest() throws Exception
	{
		String json = new ObjectMapper().writeValueAsString(new TestNto());
		mockMvc.perform(post("/test/HandlerMethodValidationException").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
	}
	
	static class TestNto
	{
		public String field = "four";
	}
}
