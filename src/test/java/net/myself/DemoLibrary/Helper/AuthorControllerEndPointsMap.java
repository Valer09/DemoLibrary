package net.myself.DemoLibrary.Helper;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@Component
public class AuthorControllerEndPointsMap
{
	private final ControllerEndPointPathBuilder pathBuilder;
	
	public AuthorControllerEndPointsMap(String lastApiVersion)
	{
		this.pathBuilder = new ControllerEndPointPathBuilder("/authors", lastApiVersion);
	}
	
	public MockHttpServletRequestBuilder addBook(String jsonAuthor) { return pathBuilder.buildPostWithJwt("").contentType(MediaType.APPLICATION_JSON).accept(pathBuilder.getAcceptString()).content(jsonAuthor);}
	public MockHttpServletRequestBuilder finByIsni(String isni) { return pathBuilder.buildGetWithJwt("findByIsni").contentType(MediaType.APPLICATION_JSON).accept(pathBuilder.getAcceptString()).param("isni", isni);}
	public MockHttpServletRequestBuilder searchByName(String name)	{ return pathBuilder.buildGetWithJwt("searchByName").contentType(MediaType.APPLICATION_JSON).accept(pathBuilder.getAcceptString()).param("name", name);}
	public MockHttpServletRequestBuilder updateIsni(String isni, String newIsni)
	{
		return pathBuilder.buildPutWithJwt("updateIsni").contentType(MediaType.APPLICATION_JSON).accept(pathBuilder.getAcceptString()).param("isni", isni).param("newIsni", newIsni);
	}
	public MockHttpServletRequestBuilder updateAuthor(String jsonAuthorUpdateNto)
	{
		return pathBuilder.buildPutWithJwt("update").contentType(MediaType.APPLICATION_JSON).accept(pathBuilder.getAcceptString()).content(jsonAuthorUpdateNto);
	}
}
