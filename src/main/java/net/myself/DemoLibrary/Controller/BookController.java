package net.myself.DemoLibrary.Controller;

import net.myself.DemoLibrary.Data.Entities.Book;
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
	
	@GetMapping("/findByTitle")
	public List<Book> findByTitle(@RequestParam("title") String title)
	{
		return _bookRepository.findByTitle(title);
	}
	
	@GetMapping("/searchByTitle")
	public List<Book> searchByTitle(@RequestParam("title") String title) {
		return _bookRepository.findByTitleContaining(title);
	}
	
	@PostMapping
	public ResponseEntity<Book> addBook(@RequestBody Book book) {
		try {
			Book savedBook = _bookRepository.save(book);
			return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
