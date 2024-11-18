package net.myself.DemoLibrary.Data.Repository;

import net.myself.DemoLibrary.Data.Entities.Book;
import net.myself.DemoLibrary.Data.Entities.DeletedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeletedBookRepository extends JpaRepository<DeletedBook, Long>
{
}
