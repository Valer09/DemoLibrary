package net.myself.DemoLibrary.Service;

import jakarta.transaction.Transactional;
import net.myself.DemoLibrary.Data.Entities.Author;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.NTO.AuthorUpdateNto;
import net.myself.DemoLibrary.Data.Repository.IAuthorRepository;
import net.myself.DemoLibrary.Model.AuthorUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService
{
	@Autowired
	private IAuthorRepository authorRepository;
	
	public boolean existsByIsni(String isni)
	{
		return authorRepository.existsByIsni(isni);
	}
	
	@Transactional
	public ServiceResponse<AuthorNto> addAuthorNto(AuthorNto authorNto)
	{
		if (authorRepository.existsByIsni(authorNto.isni())) return ServiceResponse.createError(ServiceResult.CONFLICT, "author already exists");
		return ServiceResponse.createOk(AuthorNto.fromAuthor(authorRepository.save(Author.createTransientAuthor(authorNto))));
	}
	
	public List<AuthorNto> getAllAuthorsNto()
	{
		return authorRepository.findAll().stream().map(AuthorNto::fromAuthor).collect(Collectors.toList());
	}
	
	public ServiceResponse<AuthorNto> findAuthorByIsniNto(String isni)
	{
		return authorRepository.findByIsni(isni)
						.map(author ->ServiceResponse.createOk(AuthorNto.fromAuthor(author)))
						.orElse(ServiceResponse.createError(ServiceResult.NOT_FOUND, "Author not found"));
	}
	
	public ServiceResponse<Author> findAuthorByIsni(String isni)
	{
		return authorRepository.findByIsni(isni)
						.map(ServiceResponse::createOk)
						.orElse(ServiceResponse.createError(ServiceResult.NOT_FOUND, "Author not found"));
	}
	
	@Transactional
	public ServiceResponse<AuthorNto> updateAuthorFromNto(AuthorUpdateNto authorUpdateNto)
	{
		if(!authorRepository.existsByIsni(authorUpdateNto.isni())) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "Author not found");
		var author = authorRepository.findByIsni(authorUpdateNto.isni());
		
		author.get().update(new AuthorUpdate(authorUpdateNto.name(), authorUpdateNto.lastName(), authorUpdateNto.birth()));
		
		return author.
						map(value -> ServiceResponse.createOk(AuthorNto.fromAuthor(authorRepository.save(value))))
						.orElseGet(() -> ServiceResponse.createError(ServiceResult.SERVER_ERROR, "Internal server error"));
		
	}
	
	@Transactional
	public ServiceResponse<Integer> updateIsni(String isni, String newIsni)
	{
		if(!authorRepository.existsByIsni(isni)) return ServiceResponse.createError(ServiceResult.NOT_FOUND, "Author not found");
		if(authorRepository.existsByIsni(newIsni)) return ServiceResponse.createError(ServiceResult.CONFLICT, "Isni already existing");
		var author = authorRepository.findByIsni(isni);
		
		var result = authorRepository.updateIsniById(author.get().getId(), newIsni);
		if(result != 1) return ServiceResponse.createError(ServiceResult.SERVER_ERROR, "Internal Server Error");
		
		return ServiceResponse.createOk(result);
	}
}
