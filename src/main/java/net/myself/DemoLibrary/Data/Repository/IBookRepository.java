package net.myself.DemoLibrary.Data.Repository;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import net.myself.DemoLibrary.Data.Entities.Author;
import net.myself.DemoLibrary.Data.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long>
{
	//define custom query
	List<Book> findByTitle(@Size(min = 1, max = 40) String title);
	
	List<Book> findByTitleContainingIgnoreCase(@Size(min = 1, max = 40) String title);
	
	List<Book> findByTitleAndIsbn(@Size(min = 1, max = 40) String title,  @Size(min = 13, max = 13) String isbn);
	
	Optional<Book> findByIsbn(@Size(min = 13, max = 13) String isbn);
	
	boolean existsByIsbn(@Size(min = 13, max = 13) String isbn);
	
	void deleteByIsbn(@Size(min = 13, max = 13) String isbn);
	
	@Transactional
	@Modifying
	@Query("UPDATE Book b SET b.isbn = :newIsbn WHERE b.id = :bookId")
	int updateIsbnById(@Positive long bookId, @Size(min = 13, max = 13)String newIsbn);
	
	List<Book> findByAuthor(Author author);
}
