package org.bio2schema.pipeline.pubmed;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonMutators.set;
import static org.bio2schema.util.JsonMutators.with;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static org.bio2schema.vocab.JsonLd.ID;
import static org.bio2schema.vocab.JsonLd.TYPE;
import static org.bio2schema.vocab.SchemaOrg.*;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_AFFILIATION;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_ALTERNATE_NAME;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_AUTHOR;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_NAME;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.bio2schema.api.pipeline.Processor;
import org.bio2schema.api.recognition.EntityRecognizer;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class AuthorAffiliationProcessor implements Processor {

  private final EntityRecognizer organizationRecognizer;
  private final EntityReconciler<GenericEntity> organizationReconciler;

  public AuthorAffiliationProcessor(@Nonnull EntityRecognizer organizationRecognizer,
      @Nonnull EntityReconciler<GenericEntity> organizationReconciler) {
    this.organizationRecognizer = checkNotNull(organizationRecognizer);
    this.organizationReconciler = checkNotNull(organizationReconciler);
  }

  @Override
  public JsonNode process(JsonNode input) throws IOException {
    checkIfObjectNode(input);
    JsonNode author = input.path(PROPERTY_AUTHOR);
    if (author.isArray()) {
      processAuthor((ArrayNode) author);
    }
    return input;
  }

  public void processAuthor(ArrayNode authorList) throws IOException {
    for (JsonNode author : authorList) {
      checkIfObjectNode(author);
      JsonNode affiliation = author.path(PROPERTY_AFFILIATION);
      if (!affiliation.isMissingNode()) {
        reconcileAffiliation(affiliation);
      }
    }
  }

  private void reconcileAffiliation(JsonNode affiliation) throws IOException {
    checkIfObjectNode(affiliation);
    String affiliationText = affiliation.path(PROPERTY_ALTERNATE_NAME).asText();
    Collection<String> organizationNames = organizationRecognizer.classify(affiliationText);
    Optional<GenericEntity> foundItem = iterateAndReconcileKnownOrganization(organizationNames);
    if (foundItem.isPresent()) {
      GenericEntity organization = foundItem.get();
      set(affiliation, with(ID, organization.getId()));
      if (!TYPE_THING.equals(organization.getType())) {
        set(affiliation, with(TYPE, organization.getType()));
      }
      set(affiliation, with(PROPERTY_NAME, organization.getName()));
      set(affiliation, with(PROPERTY_ALTERNATE_NAME, affiliationText));
    }
  }

  private Optional<GenericEntity> iterateAndReconcileKnownOrganization(
      Collection<String> organizationNames) throws IOException {
    for (String organizationName : organizationNames) {
      Optional<GenericEntity> result = organizationReconciler.reconcile(organizationName);
      if (result.isPresent()) {
        return result;
      }
    }
    return Optional.empty();
  }
}
