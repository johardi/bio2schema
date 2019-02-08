package org.bio2schema.reconcilers.dbpedia;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.stream.Collectors;
import org.bio2schema.api.reconciliation.RemoteDatabase;
import com.google.common.base.Charsets;

public final class DbpediaService extends RemoteDatabase {

  private final Properties serviceParameters = new Properties();

  public DbpediaService() {
    super("DBpedia Lookup");
    setDefaultParameters();
  }

  private void setDefaultParameters() {
    serviceParameters.put("MaxHits", "3");
  }

  public String getServiceEndpoint() {
    return "http://lookup.dbpedia.org/api/search/KeywordSearch";
  }

  @Override
  protected URL constructQueryUrl(String searchKeyword) throws IOException {
    StringBuilder sb = new StringBuilder("QueryString=");
    sb.append(URLEncoder.encode(searchKeyword, Charsets.UTF_8)).append("&");
    sb.append(serviceParameters.entrySet()
        .stream()
        .map(e -> e.getKey() + "=" + e.getValue())
        .collect(Collectors.joining("&")));
    return new URL(getServiceEndpoint() + "?" + sb.toString());
  }
}
