package net.myself.DemoLibrary.Controller;

import jakarta.transaction.Transactional;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Repository.BookRepository;
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
	BookRepository _bookRepository;
	
	@GetMapping
	public List<Book> getAllBooks()
	{
		return _bookRepository.findAll();
	}
	
	@GetMapping("/findByIsbn")
	public List<Book> findByIsbn(@RequestParam("isbn") String isbn)
	{
		return _bookRepository.findByIsbn(isbn);
	}
	
	@GetMapping("/findByTitle")
	public List<Book> findByTitle(@RequestParam("title") String title)
	{
		return _bookRepository.findByTitle(title);
	}
	
	@GetMapping("/searchByTitle")
	public List<Book> searchByTitle(@RequestParam("title") String title)
	{
		return _bookRepository.findByTitleContaining(title);
	}
	
	@PostMapping
	public ResponseEntity<Book> addBook(@RequestBody Book book)
	{
		try
		{
			Book savedBook = _bookRepository.save(book);
			return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
		} catch (Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Transactional
	@DeleteMapping("/isbn/{isbn}")
	public ResponseEntity<String> deleteBookByIsbn(@PathVariable String isbn)
	{
		if (!_bookRepository.existsByIsbn(isbn)) return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
		if(_bookRepository.findByIsbn(isbn).size() > 1) return new ResponseEntity<>("Server error", HttpStatus.CONFLICT);
		
		_bookRepository.deleteByIsbn(isbn);
		return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBookById(@PathVariable Long id)
	{
		if (!_bookRepository.existsById(id)) return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
		
		_bookRepository.deleteById(id);
		return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteBook(@RequestBody Book book)
	{
		List<Book> books = _bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn());
		
		if (books.isEmpty()) return new ResponseEntity<>("No book found with the given title and ISBN", HttpStatus.NOT_FOUND);
		else if (books.size() > 1) return new ResponseEntity<>("Multiple books found. Please specify more criteria.", HttpStatus.CONFLICT);
		
		_bookRepository.delete(books.get(0));
		return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
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
