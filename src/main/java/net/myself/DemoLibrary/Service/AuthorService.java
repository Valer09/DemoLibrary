package net.myself.DemoLibrary.Service;

import jakarta.transaction.Transactional;
import net.myself.DemoLibrary.Data.Entities.Author;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.Repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO: Implements
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
	
	public Optional<Author> findAuthorByCf(String cf)
	{
		return authorRepository.findByIsni(cf);
	}
}
