package org.bio2schema.api.reconciliation;

import java.io.IOException;

public class HttpRequestException extends IOException {

  private static final long serialVersionUID = 1L;

  private final int responseCode;

  public HttpRequestException(String message, int responseCode) {
    super(message);
    this.responseCode = responseCode;
  }

  public int getResponseCode() {
    return responseCode;
  }
}
