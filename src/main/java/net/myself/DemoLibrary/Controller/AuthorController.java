package net.myself.DemoLibrary.Controller;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Data.NTO.AuthorUpdateNto;
import net.myself.DemoLibrary.Service.AuthorService;
import net.myself.DemoLibrary.Service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/authors", produces = {"application/vnd.DemoLibrary.api.v1.0+json"})
//NOTE: you might want to use @Qualifier annotation to inherits different versions of a service connected to different version of the api (v1, v2...)
public class AuthorController
{
	@Autowired
	private AuthorService authorService;
	
	@PreAuthorize("hasRole('Admin')")
	@PostMapping
	public ResponseEntity<AuthorNto> addAuthor(@RequestBody @Valid AuthorNto authorNto)
	{
		var saved = authorService.addAuthorNto(authorNto);
		return switch(saved.getResult())
						{
							case CONFLICT -> new ResponseEntity<>(null, HttpStatus.CONFLICT);
							case OK -> new ResponseEntity<>(saved.get(), HttpStatus.CREATED);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
	
	@PreAuthorize("hasRole('User')")
	@GetMapping
	public ResponseEntity<List<AuthorNto>> getAllAuthors()
	{
		return new ResponseEntity<>(authorService.getAllAuthorsNto(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('User')")
	@GetMapping("/findByIsni")
	public ResponseEntity<AuthorNto> findAuthorByIsni(@RequestParam("isni") @Size(min = 16, max = 16) String isni)
	{
		var saved = authorService.findAuthorByIsniNto(isni);
		return switch(saved.getResult())
						{
							case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
							case OK -> new ResponseEntity<>(saved.get(), HttpStatus.OK);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
	
	@PreAuthorize("hasRole('User')")
	@GetMapping("/searchByName")
	public List<AuthorNto> searchByName(@RequestParam("name") @Size(min = 1, max = 20) String name)
	{
		return authorService.findByNameContainingIgnoreCaseNto(name);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@PutMapping("/update")
	public ResponseEntity<AuthorNto> updateAuthor(@RequestBody @Valid AuthorUpdateNto authorUpdateNto)
	{
		ServiceResponse<AuthorNto> authorServiceResponse = authorService.updateAuthorFromNto(authorUpdateNto);
		return switch(authorServiceResponse.getResult())
						{
							case OK -> new ResponseEntity<>(authorServiceResponse.get(), HttpStatus.OK);
							case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
	
	@PreAuthorize("hasRole('Admin')")
	@PutMapping("/updateIsni")
	public ResponseEntity<Integer> updateIsni(
					@RequestParam("isni") @Size(min = 16, max = 16) String isni,
					@RequestParam("newIsni") @Size(min = 16, max = 16)  String newIsni)
	{
		ServiceResponse<Integer> authorServiceResponse = authorService.updateIsni(isni, newIsni);
		return switch(authorServiceResponse.getResult())
						{
							case OK -> new ResponseEntity<>(authorServiceResponse.get(), HttpStatus.OK);
							case NOT_FOUND -> new ResponseEntity<>(authorServiceResponse.get(), HttpStatus.NOT_FOUND);
							case CONFLICT -> new ResponseEntity<>(authorServiceResponse.get(), HttpStatus.CONFLICT);
							default -> new ResponseEntity<>(authorServiceResponse.get(), HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
}
