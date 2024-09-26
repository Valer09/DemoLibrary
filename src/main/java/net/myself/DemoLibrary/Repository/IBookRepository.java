package net.myself.DemoLibrary.Repository;

import net.myself.DemoLibrary.Data.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long>
{
	//define custom query
	List<Book> findByTitle(String title);
	
	List<Book> findByTitleContaining(String title);
	
	List<Book> findByTitleAndIsbn(String title, String isbn);
	
	Optional<Book> findByIsbn(String isbn);
	
	boolean existsByIsbn(String isbn);
	
	void deleteByIsbn(String isbn);
}
