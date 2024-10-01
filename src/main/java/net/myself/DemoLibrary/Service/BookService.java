package net.myself.DemoLibrary.Service;
import jakarta.transaction.Transactional;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Infrastructure.Configuration.VisibleForTesting;
import net.myself.DemoLibrary.Infrastructure.GlobalControllerExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService
{
	private static final Logger _logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
	@Autowired
	IBookRepository bookRepository;
	public List<BookNto> getAllBooks()
	{
		return bookRepository.findAll().stream().map(BookNto::fromBook).collect(Collectors.toList());
	}
	
	public Optional<BookNto> findByIsbn(String isbn)
	{
		return bookRepository.findByIsbn(isbn).map(BookNto::fromBook);
	}
	
	public List<BookNto> findByTitle(String title)
	{
		return bookRepository.findByTitle(title).stream().map(BookNto::fromBook).collect(Collectors.toList());
	}
	
	public List<BookNto> findByTitleContainingIgnoreCase(String title)
	{
		return bookRepository.findByTitleContainingIgnoreCase(title).stream().map(BookNto::fromBook).collect(Collectors.toList());
	}
	
	@Transactional
	public ServiceResponse<BookNto> addBook(@RequestBody BookNto book)
	{
		if (bookRepository.existsByIsbn(book.isbn())) return ServiceResponse.createError(ServiceResult.CONFLICT, "book already exists");
		return ServiceResponse.createOk(BookNto.fromBook(bookRepository.save(Book.createTransientBook(book))));
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
	public ServiceResponse<String> deleteBook(BookNto book)
	{
		List<Book> books = bookRepository.findByTitleAndIsbn(book.title(), book.isbn());
		
		if (books.isEmpty()) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "No book found with the given title and ISBN");
		else if (books.size() > 1) return ServiceResponse.createError(ServiceResult.CONFLICT, "Multiple books found. Please specify more criteria.");
		
		bookRepository.delete(books.get(0));
		_logger.trace("deleted object book with id "+books.get(0).getId());
		
		return ServiceResponse.createOk("Book deleted successfully");
	}
	
	@Transactional
	public ServiceResponse<BookNto> updateBook(BookUpdateNto bookUpdateNto)
	{
		var books = bookRepository.findByTitleAndIsbn(bookUpdateNto.oldBook().title(), bookUpdateNto.oldBook().isbn());
		if(books.isEmpty()) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "Book not found");
		if(books.size() > 1) return ServiceResponse.createError(ServiceResult.CONFLICT, "Multiple books found");
		
		var book = books.get(0);
		book.update(Book.createTransientBook(bookUpdateNto.newBook()));
		var saved = bookRepository.save(book);
		
		_logger.trace(MessageFormat.format("Book with id %d updated", +saved.getId()));
		
		return ServiceResponse.createOk(BookNto.fromBook(saved));
	}
	
	/**
	 * <b>WARNING:</b>
	 * It is intended to be used internally within this layer, especially for communication with the Data layer.
	 * <p>
	 * The method will only be visible externally for testing purposes.
	 * </p>
	 */
	@VisibleForTesting
	@Transactional
	public ServiceResponse<String> deleteBookById(Long id)
	{
		if (!bookRepository.existsById(id)) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "Book not found");
		bookRepository.deleteById(id);
		_logger.trace("deleted object book with id "+id);
		return ServiceResponse.createOk("Book deleted successfully");
	}
}

