package br.edu.ifpb.dac.exception;

public class CannotDeleteAdminException extends RuntimeException {
    public CannotDeleteAdminException(String message) {
        super(message);
    }
}