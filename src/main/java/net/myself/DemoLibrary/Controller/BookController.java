package net.myself.DemoLibrary.Controller;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Service.BookService;
import net.myself.DemoLibrary.Service.ServiceResponse;
import net.myself.DemoLibrary.Service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController
{
	@Autowired
	IBookRepository _bookRepository;
	@Autowired
	BookService bookService;
	
	@GetMapping
	public ResponseEntity<List<BookNto>> getAllBooks()
	{
		return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
	}
	
	@GetMapping("/findByIsbn")
	public ResponseEntity<BookNto> findByIsbn(@RequestParam("isbn") String isbn)
	{
		return bookService.findByIsbn(isbn)
						.map(book -> new ResponseEntity<>(book, HttpStatus.OK))
						.orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/findByTitle")
	public ResponseEntity<List<BookNto>> findByTitle(@RequestParam("title") String title)
	{
		return new ResponseEntity<>(bookService.findByTitle(title), HttpStatus.OK);
	}
	
	@GetMapping("/searchByTitle")
	public ResponseEntity<List<BookNto>> searchByTitle(@RequestParam("title") String title)
	{
		return new ResponseEntity<>(bookService.findByTitleContainingIgnoreCase(title), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<BookNto> addBook(@RequestBody BookNto book)
	{
		var saved = bookService.addBook(book);
		if (saved.getResult().equals(ServiceResult.CONFLICT)) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		return new ResponseEntity<>(saved.get(), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/isbn/{isbn}")
	public ResponseEntity<String> deleteBookByIsbn(@PathVariable String isbn)
	{
		if (bookService.deleteBookByIsbn(isbn).getResult().equals(ServiceResult.NOT_FOUND))
			return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteBook(@RequestBody BookNto book)
	{
		return switch(bookService.deleteBook(book).getResult())
						{
							case OK -> new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
							case NOT_FOUND -> new ResponseEntity<>("No book found with the given title and ISBN", HttpStatus.NOT_FOUND);
							case CONFLICT -> new ResponseEntity<>("Multiple books found. Please specify more criteria.", HttpStatus.CONFLICT);
						};
	}
	
	@PutMapping("/update")
	public ResponseEntity<BookNto> updateBook(@RequestBody BookUpdateNto bookUpdateNto)
	{
		
		ServiceResponse<BookNto> bookServiceResponse = bookService.updateBook(bookUpdateNto);
		return switch(bookServiceResponse.getResult())
						{
							case OK -> new ResponseEntity<>(bookServiceResponse.get(), HttpStatus.OK);
							case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
							case CONFLICT -> new ResponseEntity<>(null, HttpStatus.CONFLICT);
						};
	}
}
