package br.edu.ifpb.dac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAdvertisementException extends RuntimeException {
    public InvalidAdvertisementException(String message) {
        super(message);
    }
}