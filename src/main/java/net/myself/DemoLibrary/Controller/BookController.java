package net.myself.DemoLibrary.Controller;
import net.myself.DemoLibrary.Data.Entities.Book;
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

//TODO: Improve test coverage; ServiceResponse test; Book-update test; Repository-cointains test; Expose NTO with no id out of controllers

@RestController
@RequestMapping("/books")
public class BookController
{
	@Autowired
	IBookRepository _bookRepository;
	@Autowired
	BookService bookService;
	
	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks()
	{
		return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
	}
	
	@GetMapping("/findByIsbn")
	public ResponseEntity<Book> findByIsbn(@RequestParam("isbn") String isbn)
	{
		return bookService.findByIsbn(isbn)
						.map(book -> new ResponseEntity<>(book, HttpStatus.OK))
						.orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/findByTitle")
	public ResponseEntity<List<Book>> findByTitle(@RequestParam("title") String title)
	{
		return new ResponseEntity<>(bookService.findByTitle(title), HttpStatus.OK);
	}
	
	@GetMapping("/searchByTitle")
	public ResponseEntity<List<Book>> searchByTitle(@RequestParam("title") String title)
	{
		return new ResponseEntity<>(bookService.findByTitleContaining(title), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Book> addBook(@RequestBody Book book)
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
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBookById(@PathVariable Long id)
	{
		if (bookService.deleteBookById(id).getResult().equals(ServiceResult.NOT_FOUND))
			return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteBook(@RequestBody Book book)
	{
		return switch(bookService.deleteBook(book).getResult())
						{
							case OK -> new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
							case NOT_FOUND -> new ResponseEntity<>("No book found with the given title and ISBN", HttpStatus.NOT_FOUND);
							case CONFLICT -> new ResponseEntity<>("Multiple books found. Please specify more criteria.", HttpStatus.CONFLICT);
						};
	}
	
	@PutMapping("/update")
	public ResponseEntity<Book> updateBook(@RequestBody BookUpdateNto bookUpdateNto)
	{
		
		ServiceResponse<Book> bookServiceResponse = bookService.updateBook(bookUpdateNto);
		return switch(bookServiceResponse.getResult())
						{
							case OK -> new ResponseEntity<>(bookServiceResponse.get(), HttpStatus.OK);
							case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
							case CONFLICT -> new ResponseEntity<>(null, HttpStatus.CONFLICT);
						};
	}
}
