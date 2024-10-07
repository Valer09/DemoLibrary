package net.myself.DemoLibrary.Data.Repository;
import net.myself.DemoLibrary.Data.Entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAuthorRepository extends JpaRepository<Author, Long>
{
	/*List<Book> findByTitle(String title);
	
	List<Book> findByTitleContainingIgnoreCase(String title);
	
	List<Book> findByTitleAndIsbn(String title, String isbn);
	
	Optional<Book> findByIsbn(String isbn);
	
	void deleteByIsbn(String isbn);*/
	
	boolean existsByIsni(String isni);
	
	Optional<Author> findByIsni(String isni);
	
	@Transactional
	@Modifying
	@Query("UPDATE Author a SET a.isni = :newIsni WHERE a.id = :authorId")
	int updateIsniById(Long authorId, String newIsni);
	List<Author> findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCase(String name, String lastname);
}
