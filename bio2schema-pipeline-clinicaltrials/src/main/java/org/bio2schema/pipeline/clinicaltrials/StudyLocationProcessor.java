package org.bio2schema.pipeline.clinicaltrials;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonMutators.set;
import static org.bio2schema.util.JsonMutators.with;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static org.bio2schema.vocab.JsonLd.ID;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_NAME;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_STUDY_LOCATION;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.bio2schema.api.pipeline.Processor;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public final class StudyLocationProcessor implements Processor {

  private final EntityReconciler<GenericEntity> reconciler;

  public StudyLocationProcessor(@Nonnull EntityReconciler<GenericEntity> reconciler) {
    this.reconciler = checkNotNull(reconciler);
  }

  @Override
  public JsonNode process(JsonNode input) throws IOException {
    checkIfObjectNode(input);
    JsonNode studyLocation = input.path(PROPERTY_STUDY_LOCATION);
    if (studyLocation.isArray()) {
      reconcileStudyLocation((ArrayNode) studyLocation);
    }
    return input;
  }

  private void reconcileStudyLocation(ArrayNode studyLocationList) throws IOException {
    for (JsonNode studyLocation : studyLocationList) {
      checkIfObjectNode(studyLocation);
      String locationName = studyLocation.get(PROPERTY_NAME).asText();
      Optional<GenericEntity> result = reconciler.reconcile(locationName);
      if (result.isPresent()) {
        GenericEntity location = result.get();
        set(studyLocation, with(ID, location.getId()));
        set(studyLocation, with(PROPERTY_NAME, location.getName()));
      }
    };
  }
}
