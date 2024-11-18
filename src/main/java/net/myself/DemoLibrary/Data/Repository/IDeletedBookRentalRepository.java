package net.myself.DemoLibrary.Data.Repository;

import net.myself.DemoLibrary.Data.Entities.DeletedBookRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDeletedBookRentalRepository extends JpaRepository<DeletedBookRental, Long>
{
	List<DeletedBookRental> findByUserId(String userId);
}
