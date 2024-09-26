package net.myself.DemoLibrary.Infrastructure;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneralException(Exception ex)
	{
		logger.error("Exception", ex.getMessage(), ex);
		return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
