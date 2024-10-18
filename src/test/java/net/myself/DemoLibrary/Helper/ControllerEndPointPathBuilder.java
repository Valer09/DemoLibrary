package net.myself.DemoLibrary.Helper;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.text.MessageFormat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ControllerEndPointPathBuilder
{
	private final String basePath;
	private final String lastApiVersion;
	
	public ControllerEndPointPathBuilder(String basePath, String lastApiVersion)
	{
		this.basePath = basePath;
		this.lastApiVersion = lastApiVersion;
	}
	
	public MockHttpServletRequestBuilder buildGetWithJwt(String path)
	{
		return get(buildPath(path)).with(getJwt());
	}
	
	public MockHttpServletRequestBuilder buildDeleteWithJwt(String path, String delParam)
	{
		return delete(buildPath(path),delParam).with(getJwt());
	}
	
	public MockHttpServletRequestBuilder buildPutWithJwt(String path)
	{
		return put(buildPath(path)).with(getJwt());
	}
	
	public MockHttpServletRequestBuilder buildPostWithJwt(String path)
	{
		return post(buildPath(path)).with(getJwt());
	}
	public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getJwt()
	{
		return jwt().jwt(builder -> builder.claim("ROLE", "ADMIN"));
	}
	public String getAcceptString()
	{
		return "application/vnd.DemoLibrary.api.v"+lastApiVersion+"+json";
	}
	private String buildPath(String path)
	{
		if(path.isEmpty()) return basePath;
		return MessageFormat.format("{0}/{1}", basePath, path);
	}
}
