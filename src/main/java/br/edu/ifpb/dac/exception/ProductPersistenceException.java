package br.edu.ifpb.dac.exception;

public class ProductPersistenceException extends RuntimeException {
    public ProductPersistenceException(String message) {
        super(message);
    }
}