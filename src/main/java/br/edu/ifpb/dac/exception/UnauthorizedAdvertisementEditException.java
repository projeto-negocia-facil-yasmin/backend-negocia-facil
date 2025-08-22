package br.edu.ifpb.dac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedAdvertisementEditException extends RuntimeException {
    public UnauthorizedAdvertisementEditException(String message) {
        super(message);
    }
}