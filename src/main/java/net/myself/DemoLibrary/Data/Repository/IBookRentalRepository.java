package net.myself.DemoLibrary.Data.Repository;


import jakarta.validation.constraints.Size;
import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.Entities.BookRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookRentalRepository extends JpaRepository<BookRental, Long>
{
	List<BookRental> findByUserId(String userId);
	
	Optional<BookRental> findByBookAndState(Book book, String rented);
	Optional<BookRental> findByBook(Book book);
}
