package com.slgames.store.infra;

import java.util.Arrays;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.slgames.store.model.GenreName;



@RestControllerAdvice
public class ErrorHandler {

	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> retriveCannotDelete(DataIntegrityViolationException ex){
		String mes = ex.getLocalizedMessage();
		if (mes.contains("enterprises") && mes.contains("games")) {
			mes = "Cannot delete enterprise because there are games referenced to it";
		} else if (mes.contains("enterprises") && mes.contains("name")){
			mes = "Cannot update enterprise name with the provided value, because the name already exists.";
		} else if (mes.contains("users")) mes = "Some data provided already exist on database";
		return ResponseEntity.internalServerError().body(new ExceptionBody(mes));
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> invalidateParameter(IllegalArgumentException illegalEx){
		String mes = buildMessage(illegalEx.getMessage());
		return ResponseEntity.badRequest().body(new ExceptionBody(mes));
	}
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> invalidateEnumValue(HttpMessageNotReadableException httpEx){
		String mes = buildMessage(httpEx.getMessage());
		return ResponseEntity.badRequest().body(new ExceptionBody(mes));
	}

	private String buildMessage(String mes) {
		if (mes.contains("Genre")) {
			mes = "No such Genre. Value should be in %s".formatted(Arrays.toString(GenreName.values()));
		}
		return mes;
	}
	
	record ExceptionBody(String message){
		ExceptionBody() {
			this("Some unreconigzed error has occured");
		}
	}
}
