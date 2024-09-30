package net.myself.DemoLibrary.Unit.Controller;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController
{
	@GetMapping("/entity-not-found")
	public String throwEntityNotFoundException() {
		throw new EntityNotFoundException("Entity not found test exception");
	}
	
	@GetMapping("/data-integrity-violation")
	public String throwDataIntegrityViolationException() {
		throw new DataIntegrityViolationException("Data integrity test exception");
	}
	
	@GetMapping("/general-exception")
	public String throwGeneralException() {
		throw new RuntimeException("General test exception");
	}
}
