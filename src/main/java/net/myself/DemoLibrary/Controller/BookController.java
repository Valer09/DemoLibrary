package net.myself.DemoLibrary.Controller;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Service.BookService;
import net.myself.DemoLibrary.Service.ServiceResponse;
import net.myself.DemoLibrary.Service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/books", produces = {"application/vnd.DemoLibrary.api.v1.0+json"})
public class BookController
{
	@Autowired
	BookService bookService;
	
	@PreAuthorize("hasRole('User')")
	@GetMapping
	public ResponseEntity<List<BookNto>> getAllBooks()
	{
		return new ResponseEntity<>(bookService.getAllBooksNto(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Admin') and hasAuthority('updatebook')")
	@PostMapping
	public ResponseEntity<BookNto> addBook(@RequestBody @Valid  BookNto book)
	{
		var saved = bookService.addBookFromNto(book);
		return switch(saved.getResult())
						{
							case CONFLICT -> new ResponseEntity<>(null, HttpStatus.CONFLICT);
							case OK -> new ResponseEntity<>(saved.get(), HttpStatus.CREATED);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
	
	@PreAuthorize("hasRole('User')")
	@GetMapping("/findByIsbn")
	public ResponseEntity<BookNto> findByIsbn(@RequestParam("isbn") @Size(min = 13, max = 13)  String isbn)
	{
		return bookService.findByIsbnNto(isbn)
						.map(book -> new ResponseEntity<>(book, HttpStatus.OK))
						.orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}
	
	@PreAuthorize("hasRole('User')")
	@GetMapping("/findByTitle")
	public ResponseEntity<List<BookNto>> findByTitle(@RequestParam("title") @Size(min = 1, max = 40) String title)
	{
		return new ResponseEntity<>(bookService.findByTitleNto(title), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('User')")
	@GetMapping("/findByAuthor")
	public ResponseEntity<List<BookNto>> findByAuthor(@RequestParam("isni") @Size(min = 16, max = 16) String isni)
	{
		var serviceResponse = bookService.findByAuthor(isni);
		if(serviceResponse.getResult().equals(ServiceResult.NOT_FOUND)) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(serviceResponse.get(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('User')")
	@GetMapping("/searchByTitle")
	public ResponseEntity<List<BookNto>> searchByTitle(@RequestParam("title") @Size(min = 1, max = 40) String title)
	{
		return new ResponseEntity<>(bookService.findByTitleContainingIgnoreCaseNto(title), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@DeleteMapping("/isbn/{isbn}")
	public ResponseEntity<String> deleteBookByIsbn(@PathVariable @Size(min = 13, max = 13) String isbn)
	{
		if (bookService.deleteBookByIsbn(isbn).getResult().equals(ServiceResult.NOT_FOUND))
			return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@PutMapping("/update")
	public ResponseEntity<BookNto> updateBook(@RequestBody @Valid  BookUpdateNto bookUpdateNto)
	{
		ServiceResponse<BookNto> bookServiceResponse = bookService.updateBookFromNto(bookUpdateNto);
		return switch(bookServiceResponse.getResult())
						{
							case OK -> new ResponseEntity<>(bookServiceResponse.get(), HttpStatus.OK);
							case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
	
	@PreAuthorize("hasRole('Admin')")
	@PutMapping("/updateIsbn")
	public ResponseEntity<Integer> updateIsbn(
					@RequestParam @Size(min = 13, max = 13) String isbn,
					@RequestParam @Size(min = 13, max = 13) String newIsbn)
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
