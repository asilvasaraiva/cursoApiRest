package com.alexsaraiva.cursomc.resources.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyErrorTest extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public MyErrorTest(String message) {
		super(message);
	}
	

}
