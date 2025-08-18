package br.edu.ifpb.dac.exception;

public class AdvertisementNotFoundException extends RuntimeException {
  public AdvertisementNotFoundException(String message) {
    super(message);
  }
}
