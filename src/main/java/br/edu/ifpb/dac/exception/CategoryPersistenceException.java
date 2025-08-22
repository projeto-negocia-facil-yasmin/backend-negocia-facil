package br.edu.ifpb.dac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryPersistenceException extends RuntimeException {
    public CategoryPersistenceException(String message) {
        super(message);
    }
}