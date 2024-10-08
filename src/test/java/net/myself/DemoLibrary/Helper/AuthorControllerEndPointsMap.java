package net.myself.DemoLibrary.Helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.RequestBuilder;

import java.text.MessageFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class AuthorControllerEndPointsMap
{
	private static final String basePath = "/authors";
	@Value("${version.lastApiVersion}")
	private String lastApiVersion;
	
	public RequestBuilder addBook(String jsonAuthor) {return post(basePath).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString()).content(jsonAuthor);}
	
	private String buildPath(String path)
	{
		return MessageFormat.format("{0}/{1}", basePath, path);
	}
	private String getAcceptString() {	return "application/vnd.DemoLibrary.api.v"+lastApiVersion+"+json";	}
}
