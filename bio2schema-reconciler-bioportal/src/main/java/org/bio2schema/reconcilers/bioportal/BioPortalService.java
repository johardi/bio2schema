package org.bio2schema.reconcilers.bioportal;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.bio2schema.api.reconciliation.RemoteDatabase;
import com.google.common.base.Charsets;

public class BioPortalService extends RemoteDatabase {

  private final String apiKey;

  private final Properties serviceParameters = new Properties();

  public BioPortalService() {
    this(System.getenv("BIOPORTAL_APIKEY"));
  }

  public BioPortalService(@Nonnull String apiKey) {
    super("BioPortal Search");
    this.apiKey = checkNotNull(apiKey);
    setDefaultParameters();
  }

  private void setDefaultParameters() {
    serviceParameters.put("ontologies", "MESH");
    serviceParameters.put("include", "prefLabel,synonym,definition,notation,cui,semanticType");
    serviceParameters.put("require_exact_match", "true");
    serviceParameters.put("display_links", "false");
    serviceParameters.put("page_size", "1");
  }

  /**
   * Sets a list of ontology abbreviations which will be used by BioPortal to narrow its searching.
   *
   * @param ontologies A list of target ontologies separated by a comma (e.g., "MESH,ICD10CM")
   */
  public void setTargetOntologies(String ontologies) {
    serviceParameters.put("ontologies", ontologies);
  }

  public String getServiceEndpoint() {
    return "http://data.bioontology.org/search";
  }

  @Override
  protected URL constructQueryUrl(String searchKeyword) throws IOException {
    StringBuilder sb = new StringBuilder("q=");
    sb.append(URLEncoder.encode(searchKeyword, Charsets.UTF_8)).append("&");
    sb.append(serviceParameters.entrySet()
        .stream()
        .map(e -> e.getKey() + "=" + e.getValue())
        .collect(Collectors.joining("&")));
    return new URL(getServiceEndpoint() + "?" + sb.toString());
  }

  @Override
  protected String getAuthorizationToken() {
    return "apikey token=" + apiKey;
  }
}
