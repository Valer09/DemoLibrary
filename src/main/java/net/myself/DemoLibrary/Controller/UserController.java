package net.myself.DemoLibrary.Controller;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.myself.DemoLibrary.Data.NTO.AuthUserNto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController
{
	@Value("${auth0.token-url}")
	private String tokenUrl;
	
	@Value("${auth0.client-id}")
	private String clientId;
	
	@Value("${auth0.client-secret}")
	private String clientSecret;
	
	@Value("${auth0.audience}")
	private String audience;
	
	@Autowired
	ObjectMapper jackson;
	
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/getUsers")
	public ResponseEntity<List<AuthUserNto>> getUsers()
	{
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("grant_type", "client_credentials");
		requestBody.add("client_id", clientId);
		requestBody.add("client_secret", clientSecret);
		requestBody.add("audience", "https://dev-ijzwld8inlw3zyy2.eu.auth0.com/api/v2/");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);
		
		ResponseEntity<Map> response = restTemplate.postForEntity("https://dev-ijzwld8inlw3zyy2.eu.auth0.com/oauth/token", entity, Map.class);
		
		var token = response.getBody().get("access_token");
		
		headers.clear();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("authorization", String.format("Bearer "+token.toString()));
		
		
		HttpEntity<String> entity2 = new HttpEntity<>("parameters", headers);
		ResponseEntity<String> response2 = restTemplate.exchange("https://dev-ijzwld8inlw3zyy2.eu.auth0.com/api/v2/users", HttpMethod.GET, entity2,  String.class);
		
		try
		{
			List<AuthUserNto> result = jackson.readValue(response2.getBody(), new TypeReference<>(){});
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		catch(Exception e)
		{
			System.out.println(e);
			return null;
		}
	}
}
