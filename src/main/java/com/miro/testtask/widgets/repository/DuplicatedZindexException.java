package com.miro.testtask.widgets.repository;

public class DuplicatedZindexException extends RuntimeException {
  public DuplicatedZindexException(String message) {
    super(message);
  }

  public DuplicatedZindexException(Exception e) {
    super(e);
  }
}
