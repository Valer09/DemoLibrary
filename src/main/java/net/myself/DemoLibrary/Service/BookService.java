package net.myself.DemoLibrary.Service;
import jakarta.transaction.Transactional;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Infrastructure.GlobalControllerExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class BookService
{
	private static final Logger _logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
	@Autowired
	IBookRepository bookRepository;
	public List<Book> getAllBooks()
	{
		return bookRepository.findAll();
	}
	
	public Optional<Book> findByIsbn(String isbn)
	{
		return bookRepository.findByIsbn(isbn);
	}
	
	public List<Book> findByTitle(String title)
	{
		return bookRepository.findByTitle(title);
	}
	
	public List<Book> findByTitleContaining(String title)
	{
		return bookRepository.findByTitleContaining(title);
	}
	
	@Transactional
	public ServiceResponse<Book> addBook(@RequestBody Book book)
	{
		if (bookRepository.existsByIsbn(book.getIsbn())) return ServiceResponse.createError(ServiceResult.CONFLICT, "book already exists");
		return ServiceResponse.createOk(bookRepository.save(book));
	}
	
	@Transactional
	public ServiceResponse<String> deleteBookByIsbn(String isbn)
	{
		if (!bookRepository.existsByIsbn(isbn)) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "Book not found");
		bookRepository.deleteByIsbn(isbn);
		_logger.trace("deleted object book with isbn "+isbn);
		return ServiceResponse.createOk("Book deleted successfully");
	}
	
	@Transactional
	public ServiceResponse<String> deleteBookById(Long id)
	{
		if (!bookRepository.existsById(id)) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "Book not found");
		bookRepository.deleteById(id);
		_logger.trace("deleted object book with id "+id);
		return ServiceResponse.createOk("Book deleted successfully");
	}
	
	@Transactional
	public ServiceResponse<String> deleteBook(Book book)
	{
		List<Book> books = bookRepository.findByTitleAndIsbn(book.getTitle(), book.getIsbn());
		
		if (books.isEmpty()) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "No book found with the given title and ISBN");
		else if (books.size() > 1) return ServiceResponse.createError(ServiceResult.CONFLICT, "Multiple books found. Please specify more criteria.");
		
		bookRepository.delete(books.get(0));
		_logger.trace("deleted object book with id "+book.getId());
		
		return ServiceResponse.createOk("Book deleted successfully");
	}
	
	@Transactional
	public ServiceResponse<Book> updateBook(BookUpdateNto bookUpdateNto)
	{
		var books = bookRepository.findByTitleAndIsbn(bookUpdateNto.oldBook().getTitle(), bookUpdateNto.oldBook().getIsbn());
		if(books.isEmpty()) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "Book not found");
		if(books.size() > 1) return ServiceResponse.createError(ServiceResult.CONFLICT, "Multiple books found");
		
		var book = books.get(0);
		book.update(bookUpdateNto.newBook());
		var saved = bookRepository.save(book);
		
		_logger.trace(MessageFormat.format("Book with id %d updated", +book.getId()));
		
		return ServiceResponse.createOk(saved);
	}
}

