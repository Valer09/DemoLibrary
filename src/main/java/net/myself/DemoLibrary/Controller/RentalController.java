package net.myself.DemoLibrary.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import net.myself.DemoLibrary.Data.NTO.BookNto;
import net.myself.DemoLibrary.Data.NTO.BookRentalNto;
import net.myself.DemoLibrary.Data.NTO.BookUpdateNto;
import net.myself.DemoLibrary.Service.BookService;
import net.myself.DemoLibrary.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rentals", produces = {"application/vnd.DemoLibrary.api.v1.0+json"})
public class RentalController
{
	@Autowired
	BookService bookService;
	@Autowired
	UserService userService;
	
	@PreAuthorize("hasRole('User')")
	@GetMapping
	public ResponseEntity<List<BookRentalNto>> getAllRentals()
	{
		var serviceResponse = bookService.getAllRentals(userService.getUserIdFromToken());
		if(!serviceResponse.isOk()) return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(serviceResponse.get(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('User')")
	@PostMapping
	public ResponseEntity<BookRentalNto> rentBook(@RequestBody @Valid BookRentalNto bookRentalNto)
	{
		
		String userIdFromToken = userService.getUserIdFromToken();
		var serviceResponse = bookService.rentBook(userIdFromToken, bookRentalNto);
		return switch(serviceResponse.getResult())
		{
			case OK -> new ResponseEntity<>(serviceResponse.get(), HttpStatus.CREATED);
			case CONFLICT -> new ResponseEntity<>(null, HttpStatus.CONFLICT);
			case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		};
	}
	
	@PreAuthorize("hasRole('User')")
	@GetMapping("/getBookAvailability/{isbn}")
	public ResponseEntity<String> getBookAvailability(@PathVariable @Size(min = 13, max = 13) String isbn)
	{
		var serviceResponse = bookService.isRented(isbn);
		return new ResponseEntity<>(serviceResponse ? "Not available" : "Available", HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('User')")
	@PutMapping("/completeRenting")
	public ResponseEntity<BookRentalNto> completeRenting(@RequestParam @Size(min = 13, max = 13) String isbn)
	{
		String userIdFromToken = userService.getUserIdFromToken();
		var serviceResponse = bookService.completeRenting(userIdFromToken, isbn);
		return switch(serviceResponse.getResult())
						{
							case OK -> new ResponseEntity<>(serviceResponse.get(), HttpStatus.OK);
							case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
	
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/admin/getUserRentals")
	public ResponseEntity<List<BookRentalNto>> getUserRentals(@RequestParam("userId") String userId)
	{
		var serviceResponse = bookService.getAllRentals(addOauthPrefix(userId));
		if(!serviceResponse.isOk()) return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(serviceResponse.get(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Admin')")
	@PostMapping("admin/rentBookToUser")
	public ResponseEntity<BookRentalNto> rentBookToUser(@RequestBody @Valid BookRentalNto bookRentalNto, @RequestParam String userId )
	{
		
		var serviceResponse = bookService.rentBook(addOauthPrefix(userId), bookRentalNto);
		return switch(serviceResponse.getResult())
						{
							case OK -> new ResponseEntity<>(serviceResponse.get(), HttpStatus.CREATED);
							case CONFLICT -> new ResponseEntity<>(null, HttpStatus.CONFLICT);
							case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
	
	@PreAuthorize("hasRole('Admin')")
	@PutMapping("/completeRentingToUser")
	public ResponseEntity<BookRentalNto> completeRentingToUser(@RequestParam @Size(min = 13, max = 13) String isbn, @RequestParam String userId)
	{
		var serviceResponse = bookService.completeRenting(addOauthPrefix(userId), isbn);
		return switch(serviceResponse.getResult())
						{
							case OK -> new ResponseEntity<>(serviceResponse.get(), HttpStatus.OK);
							case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
							default -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
						};
	}
	
	private static String addOauthPrefix(String userId)
	{
		return "auth0|" + userId;
	}
}
