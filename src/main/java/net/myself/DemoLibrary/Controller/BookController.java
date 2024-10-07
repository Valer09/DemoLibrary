package net.myself.DemoLibrary.Controller;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Service.BookService;
import net.myself.DemoLibrary.Service.ServiceResponse;
import net.myself.DemoLibrary.Service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//TODO:
// Implements Book->Author association modification
// Test for new service and repository of author

@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController
{
	@Autowired
	BookService bookService;
	
	@GetMapping
	public ResponseEntity<List<BookNto>> getAllBooks()
	{
		return new ResponseEntity<>(bookService.getAllBooksNto(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<BookNto> addBook(@RequestBody BookNto book)
	{
		var saved = bookService.addBookFromNto(book);
		return switch(saved.getResult())
						{
							case CONFLICT -> new ResponseEntity<>(null, HttpStatus.CONFLICT);
							case OK -> new ResponseEntity<>(saved.get(), HttpStatus.CREATED);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
	
	@GetMapping("/findByIsbn")
	public ResponseEntity<BookNto> findByIsbn(@RequestParam("isbn") String isbn)
	{
		return bookService.findByIsbnNto(isbn)
						.map(book -> new ResponseEntity<>(book, HttpStatus.OK))
						.orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/findByTitle")
	public ResponseEntity<List<BookNto>> findByTitle(@RequestParam("title") String title)
	{
		return new ResponseEntity<>(bookService.findByTitleNto(title), HttpStatus.OK);
	}
	
	@GetMapping("/searchByTitle")
	public ResponseEntity<List<BookNto>> searchByTitle(@RequestParam("title") String title)
	{
		return new ResponseEntity<>(bookService.findByTitleContainingIgnoreCaseNto(title), HttpStatus.OK);
	}
	
	@DeleteMapping("/isbn/{isbn}")
	public ResponseEntity<String> deleteBookByIsbn(@PathVariable String isbn)
	{
		if (bookService.deleteBookByIsbn(isbn).getResult().equals(ServiceResult.NOT_FOUND))
			return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<BookNto> updateBook(@RequestBody BookUpdateNto bookUpdateNto)
	{
		ServiceResponse<BookNto> bookServiceResponse = bookService.updateBookFromNto(bookUpdateNto);
		return switch(bookServiceResponse.getResult())
						{
							case OK -> new ResponseEntity<>(bookServiceResponse.get(), HttpStatus.OK);
							case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
							case CONFLICT -> new ResponseEntity<>(null, HttpStatus.CONFLICT);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
	
	@PutMapping("/updateIsbn")
	public ResponseEntity<Integer> updateIsbn(@RequestParam String isbn, @RequestParam String newIsbn)
	{
		ServiceResponse<Integer> bookServiceResponse = bookService.updateIsbn(isbn, newIsbn);
		return switch(bookServiceResponse.getResult())
						{
							case OK -> new ResponseEntity<>(bookServiceResponse.get(), HttpStatus.OK);
							case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
							case CONFLICT -> new ResponseEntity<>(null, HttpStatus.CONFLICT);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
}
