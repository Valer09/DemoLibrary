package net.myself.DemoLibrary.Unit.Infrastructure;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.myself.DemoLibrary.Unit.Controller.TestController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestController.class)
@ExtendWith(SpringExtension.class)
public class GlobalControllerExceptionHandlerTest
{
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void handleEntityNotFoundTest() throws Exception
	{
		var response = mockMvc.perform(get("/test/entity-not-found")
										.with(jwt().jwt(builder -> builder.claim("ROLE", "ADMIN"))))
						.andExpect(status().isNotFound())
						.andReturn();
		Assertions.assertThat(response.getResponse().getContentAsString()).isEqualTo("Not found");
	}
	
	@Test
	void handleDataIntegrityViolationTest() throws Exception
	{
		var response = mockMvc.perform(get("/test/data-integrity-violation")
										.with(jwt().jwt(builder -> builder.claim("ROLE", "ADMIN"))))
						.andExpect(status().isConflict())
						.andReturn();
		Assertions.assertThat(response.getResponse().getContentAsString()).isEqualTo("Data Integrity Violation");
	}
	
	@Test
	void MethodArgumentNotValidExceptionTest() throws Exception
	{
		mockMvc.perform(get("/test/MethodArgumentNotValidException")
										.with(jwt().jwt(builder -> builder.claim("ROLE", "ADMIN")))
										.param("param", "four"))
						.andExpect(status().isBadRequest());
	}
	
	@Test
	void HandlerMethodValidationExceptionTest() throws Exception
	{
		String json = new ObjectMapper().writeValueAsString(new TestNto());
		mockMvc.perform(post("/test/HandlerMethodValidationException")
										.with(jwt().jwt(builder -> builder.claim("ROLE", "ADMIN"))).contentType(MediaType.APPLICATION_JSON).content(json))
						.andExpect(status().isBadRequest());
	}
	
	static class TestNto
	{
		public String field = "four";
	}
}
