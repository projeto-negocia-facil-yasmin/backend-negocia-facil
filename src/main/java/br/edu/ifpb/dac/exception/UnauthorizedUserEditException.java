package br.edu.ifpb.dac.exception;

public class UnauthorizedUserEditException extends RuntimeException {
    public UnauthorizedUserEditException(String message) {
        super(message);
    }
}
