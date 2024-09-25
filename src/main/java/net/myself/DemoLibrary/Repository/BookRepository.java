package net.myself.DemoLibrary.Repository;

import net.myself.DemoLibrary.Data.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>
{
	//define custom query
	List<Book> findByTitle(String title);
	
	List<Book> findByTitleContaining(String title);
}
