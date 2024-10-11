package net.myself.DemoLibrary.Data.Repository;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
	boolean existsByIsni(@Size(min = 16, max = 16) String isni);
	
	Optional<Author> findByIsni(@Size(min = 16, max = 16) String isni);
	
	@Transactional
	@Modifying
	@Query("UPDATE Author a SET a.isni = :newIsni WHERE a.id = :authorId")
	int updateIsniById(@Positive Long authorId, @Size(min = 16, max = 16) String newIsni);
	List<Author> findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCase(@Size(min = 1, max = 40) String name, @Size(min = 1, max = 40) String lastname);
}
