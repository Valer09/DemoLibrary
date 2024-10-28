package net.myself.DemoLibrary.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import net.myself.DemoLibrary.Data.Entities.Author;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.Entities.BookRental;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookRentalNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Data.Repository.IBookRentalRepository;
import net.myself.DemoLibrary.Data.Repository.IBookRepository;
import net.myself.DemoLibrary.Infrastructure.Configuration.VisibleForTesting;
import net.myself.DemoLibrary.Infrastructure.GlobalControllerExceptionHandler;
import net.myself.DemoLibrary.Model.BookUpdate;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.myself.DemoLibrary.Data.Entities.BookRental.RENTED;

@Service
public class BookService
{
	private static final Logger _logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
	@Autowired
	IBookRepository bookRepository;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private IBookRentalRepository _rentalRepository;
	
	public List<BookNto> getAllBooksNto()
	{
		return bookRepository.findAll().stream().map(BookNto::fromBook).collect(Collectors.toList());
	}
	
	public Optional<BookNto> findByIsbnNto(String isbn)
	{
		return bookRepository.findByIsbn(isbn).map(BookNto::fromBook);
	}
	
	public List<BookNto> findByTitleNto(String title)
	{
		return bookRepository.findByTitle(title).stream().map(BookNto::fromBook).collect(Collectors.toList());
	}
	
	public List<BookNto> findByTitleContainingIgnoreCaseNto(String title)
	{
		return bookRepository.findByTitleContainingIgnoreCase(title).stream().map(BookNto::fromBook).collect(Collectors.toList());
	}
	
	@Transactional
	public ServiceResponse<BookNto> addBookFromNto(BookNto book)
	{
		if (bookRepository.existsByIsbn(book.isbn())) return ServiceResponse.createError(ServiceResult.CONFLICT, "book already exists");
		if (!authorService.existsByIsni(book.authorIsni())) return ServiceResponse.createError(ServiceResult.SERVER_ERROR, "author not found");
		
		Author authorByCf = authorService.findAuthorByIsni(book.authorIsni()).get();
		
		Hibernate.initialize(authorByCf.getBooks());
		
		Book transientBook = Book.createTransientBook(book, authorByCf);
		Book save = bookRepository.save(transientBook);
		
		return ServiceResponse.createOk(BookNto.fromBook(save));
		
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
	public ServiceResponse<BookNto> updateBookFromNto(BookUpdateNto bookUpdateNto)
	{
		var book = bookRepository.findByIsbn(bookUpdateNto.isbn());
		if(book.isEmpty()) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "Book not found");
		
		String authorIsni = bookUpdateNto.authorIsni();
		
		if(!authorService.existsByIsni(authorIsni)) return ServiceResponse.createError(ServiceResult.SERVER_ERROR, "Author not found");
		
		var author = authorService.findAuthorByIsni(authorIsni);
		if(!author.isOk()) return ServiceResponse.createError(ServiceResult.SERVER_ERROR, "Internal Server error");
		
		Hibernate.initialize(author.get().getBooks());
		
		book.get().update(new BookUpdate(bookUpdateNto.title(), author.get(), bookUpdateNto.publishedDate()));
		var saved = bookRepository.save(book.get());
		
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
	
	public ServiceResponse<Integer> updateIsbn(String isbn, String newIsbn)
	{
		if(!bookRepository.existsByIsbn(isbn)) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "Book not found");
		if(bookRepository.existsByIsbn(newIsbn)) return ServiceResponse.createError(ServiceResult.CONFLICT, "Isbn already existing");
		var book = bookRepository.findByIsbn(isbn);
		
		var result = bookRepository.updateIsbnById(book.get().getId(), newIsbn);
		if(result != 1) return ServiceResponse.createError(ServiceResult.SERVER_ERROR, "Internal Server Error");
		
		return ServiceResponse.createOk(result);
	}
	
	@Transactional
	public ServiceResponse<BookRentalNto> rentBook(String userIdFromToken, BookRentalNto bookRentalNto)
	{
		Optional<Book> bookOpt = bookRepository.findByIsbn(bookRentalNto.isbn());
		
		if(bookOpt.isEmpty()) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "book not found");
		
		
		
		Optional<BookRental> activeRental = _rentalRepository.findByBookAndState(bookOpt.get(), RENTED);
		if(activeRental.isPresent())
			return ServiceResponse.createError(ServiceResult.CONFLICT, "the book is already rented");
		
		var saved = _rentalRepository.save(BookRental.createTransientForNewRental(userIdFromToken, bookOpt.get()));
		
		Hibernate.initialize(saved.getBook());
		Hibernate.initialize(saved.getBook().getAuthor());
		
		return ServiceResponse.createOk(BookRentalNto.createFrom(saved));
	}
	
	public ServiceResponse<List<BookRentalNto>> getAllRentals(String userIdFromToken)
	{
		
		List<BookRental> rentals = _rentalRepository.findByUserId(userIdFromToken);
		
		return ServiceResponse.createOk(rentals.stream().map(BookRentalNto::createFrom).toList());
	}
	
	@Transactional
	public ServiceResponse<BookRentalNto> completeRenting(String userIdFromToken, String isbn)
	{
		var book = bookRepository.findByIsbn(isbn);
		if(book.isEmpty()) return ServiceResponse.createError(ServiceResult.SERVER_ERROR, "book not found");
		
		var renting = _rentalRepository.findByBookAndState(book.get(), RENTED);
		if(renting.isEmpty()) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "renting not found");
		
		BookRental bookRental = renting.get();
		if(!bookRental.getState().equals(RENTED)) return ServiceResponse.createError(ServiceResult.CONFLICT, "renting is not ongoing");
		
		bookRental.completeRenting();
		
		_rentalRepository.save(bookRental);
		
		return ServiceResponse.createOk(BookRentalNto.createFrom(bookRental));
	}
	
	public ServiceResponse<List<BookNto>> findByAuthor(String isni)
	{
		if(!authorService.existsByIsni(isni)) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "Author not found");
		var author = authorService.findAuthorByIsni(isni).get();
		return ServiceResponse.createOk(bookRepository.findByAuthor(author).stream().map(BookNto::fromBook).toList());
	}
	
	public boolean isRented(String isbn)
	{
		var book = bookRepository.findByIsbn(isbn);
		if(book.isEmpty()) throw new EntityNotFoundException("book doesn't exists");
		var result = _rentalRepository.findByBookAndState(book.get(), RENTED);
		
		return result.isPresent();
	}
}

