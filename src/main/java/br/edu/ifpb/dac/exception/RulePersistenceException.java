package br.edu.ifpb.dac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RulePersistenceException extends RuntimeException {
  public RulePersistenceException(String message) {
    super(message);
  }
}
