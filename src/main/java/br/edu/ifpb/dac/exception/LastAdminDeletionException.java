package br.edu.ifpb.dac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class LastAdminDeletionException extends RuntimeException {
    public LastAdminDeletionException(String message) {
        super(message);
    }
}