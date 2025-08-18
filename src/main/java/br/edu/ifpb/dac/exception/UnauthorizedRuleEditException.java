package br.edu.ifpb.dac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedRuleEditException extends RuntimeException {
    public UnauthorizedRuleEditException(String message) {
        super(message);
    }
}