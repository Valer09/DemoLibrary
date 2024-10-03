package net.myself.DemoLibrary.Controller;

import net.myself.DemoLibrary.Data.NTO.AuthorNto;
import net.myself.DemoLibrary.Service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorController
{
	@Autowired
	private AuthorService authorService;
	@PostMapping
	public ResponseEntity<AuthorNto> addAuthor(@RequestBody AuthorNto authorNto)
	{
		var saved = authorService.addAuthorNto(authorNto);
		return switch(saved.getResult())
						{
							case CONFLICT -> new ResponseEntity<>(null, HttpStatus.CONFLICT);
							case OK -> new ResponseEntity<>(saved.get(), HttpStatus.CREATED);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
	
	@GetMapping
	public ResponseEntity<List<AuthorNto>> getAllAuthors()
	{
		return new ResponseEntity<>(authorService.getAllAuthorsNto(), HttpStatus.OK);
	}
}
