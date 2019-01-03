package org.bio2schema.pipeline.clinicaltrials;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonMutators.set;
import static org.bio2schema.util.JsonMutators.with;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static org.bio2schema.vocab.JsonLd.ID;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_NAME;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_SPONSOR;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.bio2schema.api.pipeline.Processor;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public final class StudySponsorProcessor implements Processor {

  private final EntityReconciler<GenericEntity> reconciler;

  public StudySponsorProcessor(@Nonnull EntityReconciler<GenericEntity> reconciler) {
    this.reconciler = checkNotNull(reconciler);
  }

  @Override
  public JsonNode process(JsonNode input) throws IOException {
    checkIfObjectNode(input);
    JsonNode studySponsor = input.path(PROPERTY_SPONSOR);
    if (studySponsor.isArray()) {
      reconcileStudySponsor((ArrayNode) studySponsor);
    }
    return input;
  }

  private void reconcileStudySponsor(ArrayNode studySponsorList) throws IOException {
    for (JsonNode studySponsor : studySponsorList) {
      checkIfObjectNode(studySponsor);
      String sponsorName = studySponsor.get(PROPERTY_NAME).asText();
      Optional<GenericEntity> result = reconciler.reconcile(sponsorName);
      if (result.isPresent()) {
        GenericEntity organization = result.get();
        set(studySponsor, with(ID, organization.getId()));
        set(studySponsor, with(PROPERTY_NAME, organization.getName()));
      }
    };
  }
}
