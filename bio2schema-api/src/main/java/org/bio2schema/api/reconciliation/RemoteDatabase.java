package org.bio2schema.api.reconciliation;

import static java.lang.String.format;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public abstract class RemoteDatabase implements Database {

  private static Logger logger = LogManager.getRootLogger();

  @Override
  public Object find(String label) throws IOException {
    HttpURLConnection conn = null;
    try {
      URL url = constructQueryUrl(label);
      logger.debug("Query string: {}", url);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Authorization", getAuthorizationToken());
      conn.setRequestProperty("Accept", "application/json");
      JsonNode response = getResponse(conn);
      return response;
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
  }

  protected abstract URL constructQueryUrl(String searchKeyword) throws IOException;

  protected String getAuthorizationToken() {
    return "";
  }

  private JsonNode getResponse(HttpURLConnection conn) throws IOException {
    if (conn.getResponseCode() != 200) {
      throwIOException(conn);
    }
    return JacksonUtils.mapper.readTree(conn.getInputStream());
  }

  private static void throwIOException(HttpURLConnection conn) throws IOException {
    String errorMessage = format("Service provider returns %s: %s. ",
        conn.getResponseCode(),
        conn.getResponseMessage());
    final InputStream errorStream = conn.getErrorStream();
    if (errorStream != null) {
      errorMessage += format("Details:\n%s", getStringFromStream(errorStream));
    }
    throw new IOException(errorMessage);
  }

  private static String getStringFromStream(InputStream errorStream) throws IOException {
    final Reader errorReader = new InputStreamReader(errorStream, Charsets.UTF_8);
    String message = CharStreams.toString(errorReader);
    try {
      return JacksonUtils.mapper
          .writerWithDefaultPrettyPrinter()
          .writeValueAsString(message);
    } catch(JsonProcessingException e) { // not a JSON string
      return message;
    }
  }
}
