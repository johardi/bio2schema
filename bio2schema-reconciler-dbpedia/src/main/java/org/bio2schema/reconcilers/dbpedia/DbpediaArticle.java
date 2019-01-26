package org.bio2schema.reconcilers.dbpedia;

import static org.bio2schema.vocab.SchemaOrg.TYPE_THING;
import static org.bio2schema.vocab.SchemaOrg.getTypeName;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

@SuppressWarnings("serial")
public class DbpediaArticle implements GenericEntity {

  private final String uri;
  private final String label;
  private final String description;
  private final List<ClassType> classes = Lists.newArrayList();
  private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonCreator
  public DbpediaArticle(
      @JsonProperty("uri") String uri,
      @JsonProperty("label") String label,
      @JsonProperty("description") String description,
      @JsonProperty("classes") List<ClassType> classes) {
    this.uri = uri;
    this.label = label;
    this.description = description;
    this.classes.addAll(classes);
  }

  @JsonProperty("id")
  @Override
  public String getId() {
    return uri;
  }

  @JsonProperty("type")
  @Override
  public String getType() {
    String schemaOrgType = TYPE_THING; // default type
    for (ClassType ct : classes) {
      String uri = ct.getUri();
      Optional<String> type = getTypeName(uri);
      if (type.isPresent()) {
        schemaOrgType = type.get();
      }
    }
    return schemaOrgType;
  }

  @JsonProperty("name")
  @Override
  public String getName() {
    return label;
  }

  @JsonProperty("description")
  @Override
  public String getDescription() {
    return description;
  }

  @JsonAnyGetter
  @Override
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uri, label, description, classes, additionalProperties);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof DbpediaArticle)) {
      return false;
    }
    DbpediaArticle other = (DbpediaArticle) obj;
    return uri.equals(other.uri)
        && label.equals(other.label)
        && description.equals(other.description)
        && classes.equals(other.classes)
        && additionalProperties.equals(other.additionalProperties);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("uri", uri)
        .add("label", label)
        .add("description", description)
        .add("classes", classes)
        .add("additionalProperties", additionalProperties)
        .toString();
  }

  public static class ClassType implements Serializable {

    private final String uri;
    private final String label;

    @JsonCreator
    public ClassType(@JsonProperty("uri") String uri, @JsonProperty("label") String label) {
      this.uri = uri;
      this.label = label;
    }

    @JsonProperty("uri")
    public String getUri() {
      return uri;
    }

    @JsonProperty("label")
    public String getLabel() {
      return label;
    }

    @Override
    public int hashCode() {
      return Objects.hash(uri, label);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof ClassType)) {
        return false;
      }
      ClassType other = (ClassType) obj;
      return uri.equals(other.uri) && label.equals(other.label);
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("uri", uri)
          .add("label", label)
          .toString();
    }
  }
}
