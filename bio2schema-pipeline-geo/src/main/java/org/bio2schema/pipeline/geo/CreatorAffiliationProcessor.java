package org.bio2schema.pipeline.geo;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonMutators.set;
import static org.bio2schema.util.JsonMutators.with;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static org.bio2schema.vocab.JsonLd.ID;
import static org.bio2schema.vocab.JsonLd.TYPE;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_AFFILIATION;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CREATOR;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_NAME;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_ALTERNATE_NAME;
import static org.bio2schema.vocab.SchemaOrg.TYPE_THING;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.bio2schema.api.pipeline.Processor;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class CreatorAffiliationProcessor implements Processor {

  private final EntityReconciler<GenericEntity> organizationReconciler;

  public CreatorAffiliationProcessor(
      @Nonnull EntityReconciler<GenericEntity> organizationReconciler) {
    this.organizationReconciler = checkNotNull(organizationReconciler);
  }

  @Override
  public JsonNode process(JsonNode input) throws IOException {
    checkIfObjectNode(input);
    JsonNode creatorList = input.path(PROPERTY_CREATOR);
    for (JsonNode creator : (ArrayNode) creatorList) {
      JsonNode creatorAffiliation = creator.path(PROPERTY_AFFILIATION);
      if (!creatorAffiliation.isMissingNode()) {
        reconcileCreatorAffiliation(creatorAffiliation);
      }
    }
    return input;
  }

  private void reconcileCreatorAffiliation(JsonNode affiliation) throws IOException {
    checkIfObjectNode(affiliation);
    String organizationName = affiliation.get(PROPERTY_NAME).asText();
    Optional<GenericEntity> result = organizationReconciler.reconcile(organizationName);
    if (result.isPresent()) {
      GenericEntity organization = result.get();
      set(affiliation, with(ID, organization.getId()));
      if (!TYPE_THING.equals(organization.getType())) {
        set(affiliation, with(TYPE, organization.getType()));
      }
      set(affiliation, with(PROPERTY_NAME, organization.getName()));
      if (!organization.getName().equals(organizationName)) {
        set(affiliation, with(PROPERTY_ALTERNATE_NAME, organizationName));
      }
    }
  }
}
