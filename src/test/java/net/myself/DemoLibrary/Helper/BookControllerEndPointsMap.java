package net.myself.DemoLibrary.Helper;

import net.myself.DemoLibrary.Data.Entities.Book;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@Component
public class BookControllerEndPointsMap
{
	private final ControllerEndPointPathBuilder pathBuilder;
	
	public BookControllerEndPointsMap(String lastApiVersion)
	{
		this.pathBuilder = new ControllerEndPointPathBuilder("/books", lastApiVersion);
	}
	
	public MockHttpServletRequestBuilder getAllBooks()	{ return pathBuilder.buildGetWithJwt("").contentType("lastApiVersion").accept(pathBuilder.getAcceptString());}
	public MockHttpServletRequestBuilder findByIsbn(String isbn) {return pathBuilder.buildGetWithJwt("findByIsbn").contentType(MediaType.APPLICATION_JSON).accept(pathBuilder.getAcceptString()).param("isbn", isbn);}
	public MockHttpServletRequestBuilder findByTitle(String title) {return pathBuilder.buildGetWithJwt("findByTitle").contentType(MediaType.APPLICATION_JSON).accept(pathBuilder.getAcceptString()).param("title", title);}
	public MockHttpServletRequestBuilder addBook(String jsonBook) {return pathBuilder.buildPostWithJwt("").contentType(MediaType.APPLICATION_JSON).accept(pathBuilder.getAcceptString()).content(jsonBook);}
	public MockHttpServletRequestBuilder deleteBookByIsbn(Book book) {return pathBuilder.buildDeleteWithJwt("isbn/{isbn}", book.getIsbn()).contentType(MediaType.APPLICATION_JSON).accept(pathBuilder.getAcceptString());}
	public MockHttpServletRequestBuilder updateBook(String jsonBook) {return pathBuilder.buildPutWithJwt("update").contentType(MediaType.APPLICATION_JSON).accept(pathBuilder.getAcceptString()).content(jsonBook);}
	public MockHttpServletRequestBuilder searchByTitle(String title) {return pathBuilder.buildGetWithJwt("searchByTitle").contentType(MediaType.APPLICATION_JSON).accept(pathBuilder.getAcceptString()).param("title", title);}
	public MockHttpServletRequestBuilder updateIsbn(String isbn, String newIsbn)
	{
		return pathBuilder.buildPutWithJwt("updateIsbn").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).param("isbn", isbn).accept(pathBuilder.getAcceptString()).param("newIsbn", newIsbn);
	}
}
