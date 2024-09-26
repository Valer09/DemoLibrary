package net.myself.DemoLibrary.Controller;

import jakarta.transaction.Transactional;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Repository.BookRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


//TODO: Create ErrorResponse class. Then implements @ControllerAdvice.
@RestController
@RequestMapping("/books")
public class BookController
{
	@Autowired
	BookRepository _bookRepository;
	
	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks()
	{
		try
		{
			return new ResponseEntity<>(_bookRepository.findAll(), HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/findByIsbn")
	public ResponseEntity<Book> findByIsbn(@RequestParam("isbn") String isbn)
	{
		return _bookRepository.findByIsbn(isbn)
						.map(book -> new ResponseEntity<>(book, HttpStatus.OK))
						.orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/findByTitle")
	public ResponseEntity<List<Book>> findByTitle(@RequestParam("title") String title)
	{
		try
		{
			return new ResponseEntity<>(_bookRepository.findByTitle(title), HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/searchByTitle")
	public ResponseEntity<List<Book>> searchByTitle(@RequestParam("title") String title)
	{
		try
		{
			return new ResponseEntity<>(_bookRepository.findByTitleContaining(title), HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping
	public ResponseEntity<Book> addBook(@RequestBody Book book)
	{
		
		if (_bookRepository.existsByIsbn(book.getIsbn())) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		
		try
		{
			Book savedBook = _bookRepository.save(book);
			return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Transactional
	@DeleteMapping("/isbn/{isbn}")
	public ResponseEntity<String> deleteBookByIsbn(@PathVariable String isbn)
	{
		try
		{
			if (!_bookRepository.existsByIsbn(isbn)) return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
			_bookRepository.deleteByIsbn(isbn);
			return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBookById(@PathVariable Long id)
	{
		try
		{
			if (!_bookRepository.existsById(id)) return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
			
			_bookRepository.deleteById(id);
			return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteBook(@RequestBody Book book)
	{
		try
		{
			List<Book> books = _bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn());
			
			if (books.isEmpty()) return new ResponseEntity<>("No book found with the given title and ISBN", HttpStatus.NOT_FOUND);
			else if (books.size() > 1) return new ResponseEntity<>("Multiple books found. Please specify more criteria.", HttpStatus.CONFLICT);
			
			_bookRepository.delete(books.get(0));
			return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<Book> updateBook(@RequestBody BookUpdateNto bookUpdateNto)
	{
		try
		{
			var books = _bookRepository.findByTitleAndIsbn(bookUpdateNto.oldBook().getTitle(), bookUpdateNto.oldBook().getIsbn());
			if(books.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			if(books.size() > 1) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
			
			var book = books.get(0);
			book.update(bookUpdateNto.newBook());
			
			_bookRepository.save(book);
			return new ResponseEntity<>(book, HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
