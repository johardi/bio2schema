package org.bio2schema.pipeline.geo;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonMutators.append;
import static org.bio2schema.util.JsonMutators.set;
import static org.bio2schema.util.JsonMutators.with;
import static org.bio2schema.util.JsonPreconditions.checkIfArrayNode;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static org.bio2schema.vocab.JsonLd.ID;
import static org.bio2schema.vocab.JsonLd.TYPE;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_ABOUT;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_ALTERNATE_NAME;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CODE;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CODE_VALUE;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CODING_SYSTEM;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_DATASET;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_NAME;
import static org.bio2schema.vocab.SchemaOrg.TYPE_MEDICAL_CODE;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.bio2schema.api.pipeline.Processor;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity.MedicalCode;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SampleOrganismProcessor implements Processor {

  private final EntityReconciler<MedicalEntity> reconciler;

  public SampleOrganismProcessor(@Nonnull EntityReconciler<MedicalEntity> reconciler) {
    this.reconciler = checkNotNull(reconciler);
  }

  @Override
  public JsonNode process(JsonNode input) throws IOException {
    checkIfObjectNode(input);
    JsonNode datasetList = input.path(PROPERTY_DATASET);
    for (JsonNode dataset : (ArrayNode) datasetList) {
      JsonNode datasetTopic = dataset.path(PROPERTY_ABOUT);
      reconcileDatasetTopic(datasetTopic);
    }
    return input;
  }

  private void reconcileDatasetTopic(JsonNode topicList) throws IOException {
    checkIfArrayNode(topicList);
    for (JsonNode topic : (ArrayNode) topicList) {
      String organismName = topic.get(PROPERTY_NAME).asText();
      Optional<MedicalEntity> result = reconciler.reconcile(organismName);
      if (result.isPresent()) {
        MedicalEntity medicalEntity = result.get();
        Optional<MedicalCode> medicalCode = medicalEntity.getMedicalCode();
        if (medicalCode.isPresent()) {
          ObjectNode meshCode = JacksonUtils.createEmptyObjectNode();
          set(meshCode, with(TYPE, TYPE_MEDICAL_CODE));
          set(meshCode, with(ID, medicalCode.get().getCui()));
          set(meshCode, with(PROPERTY_CODE_VALUE, medicalCode.get().getCodeValue()));
          set(meshCode, with(PROPERTY_CODING_SYSTEM, medicalCode.get().getCodingSystem()));
          set(meshCode, with(PROPERTY_NAME, medicalEntity.getName()));
          set(meshCode, with(PROPERTY_ALTERNATE_NAME, medicalEntity.getSynonyms()));
          append(topic, with(PROPERTY_CODE, meshCode));
        }
      }
    }
  }
}
