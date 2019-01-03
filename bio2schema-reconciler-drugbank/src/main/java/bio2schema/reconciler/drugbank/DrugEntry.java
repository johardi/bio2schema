package bio2schema.reconciler.drugbank;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SuppressWarnings("serial")
public class DrugEntry implements MedicalEntity {

  private final String id;
  private final String name;
  private final String synonymLabel;
  private Map<String, Object> additionalProperties = Maps.newHashMap();

  @JsonCreator
  public DrugEntry(@JsonProperty("DrugBank ID") String id,
      @JsonProperty("Common name") String name,
      @JsonProperty("Synonyms") String synonymLabel) {
    this.id = checkNotNull(id);
    this.name = checkNotNull(name);
    this.synonymLabel = checkNotNull(synonymLabel);
  }

  @JsonProperty("id")
  @Override
  public String getId() {
    return "http://identifiers.org/drugbank/" + id;
  }

  @JsonProperty("type")
  @Override
  public String getType() {
    return "Drug";
  }

  @JsonProperty("name")
  @Override
  public String getName() {
    return name;
  }

  @JsonProperty("description")
  @Nullable
  @Override
  public String getDescription() {
    return null;
  }

  @JsonProperty("medicalCode")
  @Override
  public Optional<MedicalCode> getMedicalCode() {
    return Optional.empty();
  }

  @JsonProperty("synonyms")
  @Override
  public Collection<String> getSynonyms() {
    return Lists.newArrayList(synonymLabel.split(" \\| "));
  }

  @JsonAnyGetter
  @Override
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getType(), getName(),
        getDescription(), getMedicalCode(), getSynonyms(),
        getAdditionalProperties());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof DrugEntry)) {
      return false;
    }
    DrugEntry other = (DrugEntry) obj;
    return getId().equals(other.getId())
        && getName().equals(other.getName())
        && getType().equals(other.getType())
        && getMedicalCode().equals(other.getMedicalCode());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", getId())
        .add("type", getType())
        .add("name", getName())
        .add("description", getDescription())
        .add("medicalCode", getMedicalCode())
        .add("synonyms", getSynonyms())
        .add("additionalProperties", additionalProperties)
        .toString();
  }
}
