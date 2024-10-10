package net.myself.DemoLibrary.Helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.RequestBuilder;

import java.text.MessageFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Component
public class AuthorControllerEndPointsMap
{
	private static final String basePath = "/authors";
	@Value("${version.lastApiVersion}")
	private String lastApiVersion;
	
	public RequestBuilder addBook(String jsonAuthor) { return post(basePath).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString()).content(jsonAuthor);}
	public RequestBuilder finByIsni(String isni) { return get(buildPath("findByIsni")).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString()).param("isni", isni);}
	public RequestBuilder searchByName(String name)	{ return get(buildPath("searchByName")).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString()).param("name", name);}
	public RequestBuilder updateIsni(String isni, String newIsni)
	{
		return put(buildPath("updateIsni")).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString()).param("isni", isni).param("newIsni", newIsni);
	}
	public RequestBuilder updateAuthor(String jsonAuthorUpdateNto)
	{
		return put(buildPath("update")).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString()).content(jsonAuthorUpdateNto);
	}
	private String buildPath(String path)
	{
		return MessageFormat.format("{0}/{1}", basePath, path);
	}
	private String getAcceptString() {	return "application/vnd.DemoLibrary.api.v"+lastApiVersion+"+json";	}
	
}
