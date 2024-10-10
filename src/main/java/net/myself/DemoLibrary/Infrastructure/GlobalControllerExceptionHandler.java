package net.myself.DemoLibrary.Infrastructure;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import java.util.*;

@ControllerAdvice
public class GlobalControllerExceptionHandler
{
	private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex)
	{
		logger.error("EntityNotFound exception", ex.getMessage(), ex);
		return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex)
	{
		logger.error("DataIntegrityViolation exception", ex.getMessage(), ex);
		return new ResponseEntity<>("Data Integrity Violation", HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleDataIntegrityViolation(MethodArgumentNotValidException ex)
	{
		logger.error("MethodArgumentNotValidException exception", ex.getMessage(), ex);
		
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
						.forEach(error -> {errors.put(error.getField(), error.getDefaultMessage());});
		
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<Object> handleDataIntegrityViolation(HandlerMethodValidationException ex)
	{
		logger.error("HandlerMethodValidationException exception", ex.getMessage(), ex);
		
		Map<String, List<String>> errors = new HashMap<>();
		
		ex.getAllValidationResults()
						.forEach(error -> {
							List<String> messages = new ArrayList<>();
							error.getResolvableErrors().forEach(mess -> messages.add(mess.getDefaultMessage()));
							errors.put(error.getMethodParameter().getParameterName(), messages);});
		
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	
	
}
