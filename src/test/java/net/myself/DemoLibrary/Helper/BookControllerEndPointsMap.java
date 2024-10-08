package net.myself.DemoLibrary.Helper;

import net.myself.DemoLibrary.Data.Entities.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.text.MessageFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Component
public class BookControllerEndPointsMap
{
	private static final String basePath = "/books";
	@Value("${version.lastApiVersion}")
	private String lastApiVersion;
	
	public MockHttpServletRequestBuilder getAllBooks()	{return get(basePath).contentType("lastApiVersion").accept(getAcceptString());}
	public RequestBuilder findByIbsn(String isbn) {return get(buildPath("findByIsbn")).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString()).param("isbn", isbn);}
	public MockHttpServletRequestBuilder findByTitle(String title) {return get(buildPath("findByTitle")).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString()).param("title", title);}
	public RequestBuilder addBook(String jsonBook) {return post(basePath).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString()).content(jsonBook);}
	public RequestBuilder deleteBookByIsbn(Book book) {return delete(buildPath("isbn/{isbn}"), book.getIsbn()).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString());}
	public RequestBuilder updateBook(String jsonBook) {return put(buildPath("update")).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString()).content(jsonBook);}
	public RequestBuilder searchByTitle(String title) {return get(buildPath("searchByTitle")).contentType(MediaType.APPLICATION_JSON).accept(getAcceptString()).param("title", title);}
	public RequestBuilder updateIsbn(String isbn, String newIsbn)
	{
		return put(buildPath("updateIsbn")).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).param("isbn", isbn).accept(getAcceptString()).param("newIsbn", newIsbn);
	}
	private String buildPath(String path)
	{
		return MessageFormat.format("{0}/{1}", basePath, path);
	}
	private String getAcceptString() {	return "application/vnd.DemoLibrary.api.v"+lastApiVersion+"+json";	}
}
