package br.edu.ifpb.dac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductHasAdvertisementException extends RuntimeException {
  public ProductHasAdvertisementException(String message) {
    super(message);
  }
}