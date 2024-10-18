package net.myself.DemoLibrary.Controller;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/authentication", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthenticationController
{
	@Value("${auth0.token-url}")
	private String tokenUrl;
	
	@Value("${auth0.client-id}")
	private String clientId;
	
	@Value("${auth0.client-secret}")
	private String clientSecret;
	
	@Value("${auth0.audience}")
	private String audience;
	
	@PostMapping("/login")
	public ResponseEntity<Map> login(@RequestParam("email") @Email String email, @RequestParam("password") String password)
	{
		if(!password.equals(getPsw())) return ResponseEntity.status(401).body(null);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("client_id", clientId);
		requestBody.put("client_secret", clientSecret);
		requestBody.put("audience", audience);
		requestBody.put("grant_type", "client_credentials");
		
		HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
		
		ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);
		
		return response;
	}
	
	private String getPsw()
	{
		return "password";
	}
}
