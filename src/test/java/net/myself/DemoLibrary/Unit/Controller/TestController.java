package net.myself.DemoLibrary.Unit.Controller;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.xmlunit.validation.ValidationResult;

import java.lang.reflect.Method;

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
	
	@GetMapping("/MethodArgumentNotValidException")
	public String MethodArgumentNotValidException(@Valid @Size(min = 1, max = 3) @RequestParam("param") String param)
	{
		return "ok";
	}
	
	@PostMapping(value = "/HandlerMethodValidationException", produces = {MediaType.APPLICATION_JSON_VALUE})
	public String HandlerMethodValidationException(@Valid @RequestBody TestNto body)
	{
		return "ok";
	}
	
	static class TestNto
	{
		@Size(min = 1, max = 3)
		public String field;
	}
}

