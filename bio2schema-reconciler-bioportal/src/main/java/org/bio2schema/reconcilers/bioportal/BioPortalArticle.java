package org.bio2schema.reconcilers.bioportal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.vocab.SchemaOrg.TYPE_ADMINISTRATIVE_AREA;
import static org.bio2schema.vocab.SchemaOrg.TYPE_ANATOMICAL_STRUCTURE;
import static org.bio2schema.vocab.SchemaOrg.TYPE_ANATOMICAL_SYSTEM;
import static org.bio2schema.vocab.SchemaOrg.TYPE_DRUG;
import static org.bio2schema.vocab.SchemaOrg.TYPE_MEDICAL_CONDITION;
import static org.bio2schema.vocab.SchemaOrg.TYPE_MEDICAL_DEVICE;
import static org.bio2schema.vocab.SchemaOrg.TYPE_MEDICAL_ENTITY;
import static org.bio2schema.vocab.SchemaOrg.TYPE_MEDICAL_PROCEDURE;
import static org.bio2schema.vocab.SchemaOrg.TYPE_OCCUPATION;
import static org.bio2schema.vocab.SchemaOrg.TYPE_ORGANIZATION;
import static org.bio2schema.vocab.nlm.SemanticType.ANATOMY_GROUP;
import static org.bio2schema.vocab.nlm.SemanticType.ANTIBIOTIC;
import static org.bio2schema.vocab.nlm.SemanticType.BODY_SYSTEM;
import static org.bio2schema.vocab.nlm.SemanticType.CHEMICALS_AND_DRUGS_GROUP;
import static org.bio2schema.vocab.nlm.SemanticType.CLINICAL_DRUG;
import static org.bio2schema.vocab.nlm.SemanticType.DEVICES_GROUP;
import static org.bio2schema.vocab.nlm.SemanticType.DISORDERS_GROUP;
import static org.bio2schema.vocab.nlm.SemanticType.GEOGRAPHIC_AREAS_GROUP;
import static org.bio2schema.vocab.nlm.SemanticType.OCCUPATIONS_GROUP;
import static org.bio2schema.vocab.nlm.SemanticType.ORGANIZATIONS_GROUP;
import static org.bio2schema.vocab.nlm.SemanticType.PHARMACOLOGIC_SUBSTANCE;
import static org.bio2schema.vocab.nlm.SemanticType.PROCEDURES_GROUP;
import static org.bio2schema.vocab.nlm.SemanticType.VITAMIN;
import static org.bio2schema.vocab.nlm.SemanticType.getSemanticType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.vocab.nlm.SemanticType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

@SuppressWarnings("serial")
public class BioPortalArticle implements MedicalEntity {

  private final String url;
  private final String notation;
  private final String prefLabel;
  private final List<String> synonyms;
  private final List<String> definitions;
  private final List<String> semanticTypes;
  private final Map<String, Object> additionalProperties = Maps.newHashMap();

  @JsonCreator
  public BioPortalArticle(@JsonProperty("@id") String url,
      @JsonProperty("notation") String notation, @JsonProperty("prefLabel") String prefLabel,
      @JsonProperty("synonym") List<String> synonyms,
      @JsonProperty("definition") List<String> definitions,
      @JsonProperty("semanticType") List<String> semanticTypes) {
    this.url = checkNotNull(url);
    this.notation = checkNotNull(notation);
    this.prefLabel = checkNotNull(prefLabel);
    this.synonyms = synonyms;
    this.definitions = definitions;
    this.semanticTypes = semanticTypes;
  }

  @JsonProperty("id")
  @Nullable
  @Override
  public String getId() {
    return null;
  }

  @JsonProperty("type")
  @Override
  public String getType() {
    if (semanticTypes == null) {
      return null;
    }
    for (String type : semanticTypes) {
      SemanticType semanticType = getSemanticType(type);
      if (DISORDERS_GROUP.contains(semanticType)) {
        return TYPE_MEDICAL_CONDITION;
      } else if (PROCEDURES_GROUP.contains(semanticType)) {
        return TYPE_MEDICAL_PROCEDURE;
      } else if (DEVICES_GROUP.contains(semanticType)) {
        return TYPE_MEDICAL_DEVICE;
      } else if (CHEMICALS_AND_DRUGS_GROUP.contains(semanticType)) {
        if (CLINICAL_DRUG.equals(semanticType) || PHARMACOLOGIC_SUBSTANCE.equals(semanticType)
            || ANTIBIOTIC.equals(semanticType) || VITAMIN.equals(semanticType)) {
          return TYPE_DRUG;
        }
      } else if (ANATOMY_GROUP.contains(semanticType)) {
        if (BODY_SYSTEM.equals(semanticType)) {
          return TYPE_ANATOMICAL_SYSTEM;
        } else {
          return TYPE_ANATOMICAL_STRUCTURE;
        }
      } else if (ORGANIZATIONS_GROUP.contains(semanticType)) {
        return TYPE_ORGANIZATION;
      } else if (OCCUPATIONS_GROUP.contains(semanticType)) {
        return TYPE_OCCUPATION;
      } else if (GEOGRAPHIC_AREAS_GROUP.contains(semanticType)) {
        return TYPE_ADMINISTRATIVE_AREA;
      }
    }
    return TYPE_MEDICAL_ENTITY; // default type
  }

  @JsonProperty("name")
  @Override
  public String getName() {
    return prefLabel;
  }

  @JsonProperty("description")
  @Nullable
  @Override
  public String getDescription() {
    if (definitions == null) {
      return null;
    }
    return (!definitions.isEmpty()) ? definitions.get(0) : null;
  }

  @JsonProperty("medicalCode")
  @Override
  public Optional<MedicalCode> getMedicalCode() {
    return Optional.of(new MedicalCode() {
      @JsonProperty("cui")
      @Nullable
      @Override
      public String getCui() {
        if ("MESH".equals(getCodingSystem())) {
          return "http://identifiers.org/mesh/" + notation;
        }
        return null;
      }

      @JsonProperty("codeValue")
      @Override
      public String getCodeValue() {
        return notation;
      }

      @JsonProperty("codingSystem")
      @Override
      public String getCodingSystem() {
        if (url.contains("http://purl.bioontology.org/ontology")) {
          return url.split("/")[4];
        } else if (url.contains("http://purl.obolibrary.org/obo/")) {
          return "OBO";
        } else {
          return "Unknown";
        }
      }

      @Override
      public int hashCode() {
        return Objects.hash(getCui(), getCodeValue(), getCodingSystem());
      }

      @Override
      public boolean equals(Object obj) {
        if (obj == null) {
          return false;
        }
        if (!(obj instanceof MedicalCode)) {
          return false;
        }
        MedicalCode other = (MedicalCode) obj;
        return getCui().equals(other.getCui())
            && getCodeValue().equals(other.getCodeValue())
            && getCodingSystem().equals(other.getCodingSystem());
      }

      @Override
      public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("cui", getCui())
            .add("codeValue", getCodeValue())
            .add("codingSystem", getCodingSystem())
            .toString();
      }
    });
  }

  @JsonProperty("synonyms")
  @Nullable
  @Override
  public List<String> getSynonyms() {
    return synonyms;
  }

  @JsonProperty("additionalProperties")
  @JsonAnyGetter
  @Override
  public Map<String, Object> getAdditionalProperties() {
    return Collections.unmodifiableMap(additionalProperties);
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
    if (!(obj instanceof BioPortalArticle)) {
      return false;
    }
    BioPortalArticle other = (BioPortalArticle) obj;
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
