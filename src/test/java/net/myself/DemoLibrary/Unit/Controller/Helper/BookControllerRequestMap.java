package net.myself.DemoLibrary.Unit.Controller.Helper;

import net.myself.DemoLibrary.Data.Entities.Book;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.text.MessageFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class BookControllerRequestMap
{
	private static final String basePath = "/books";
	
	public static MockHttpServletRequestBuilder getAllBooks()	{	return get(basePath).contentType(MediaType.APPLICATION_JSON);	}
	public static RequestBuilder findByIbsn() {return get(buildPath("findByIsbn")).contentType(MediaType.APPLICATION_JSON).param("isbn", "abcd");}
	public static MockHttpServletRequestBuilder findByTitle(){ return get(buildPath("findByTitle")).contentType(MediaType.APPLICATION_JSON).param("title", "title");}
	public static RequestBuilder addBook(String jsonBook){return post(basePath).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonBook);	}
	public static RequestBuilder deleteBookByIsbn(Book book){return delete(buildPath("isbn/{isbn}"), book.getIsbn()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);}
	public static RequestBuilder deleteBookById(Book book)	{return delete(buildPath("{id}"), book.getId()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);	}
	public static RequestBuilder updateBook(String jsonBook){return put(buildPath("update")).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonBook);}
	
	private static String buildPath(String path)
	{
		return MessageFormat.format("{0}/{1}", basePath, path);
	}
	
	public static RequestBuilder deleteBook(String jsonBook)
	{
		return delete(buildPath("delete"))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(jsonBook);
	}
}
