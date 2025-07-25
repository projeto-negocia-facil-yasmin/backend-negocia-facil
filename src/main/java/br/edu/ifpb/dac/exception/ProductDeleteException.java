package br.edu.ifpb.dac.exception;

public class ProductDeleteException extends RuntimeException {
    public ProductDeleteException(String message) {
        super(message);
    }
}