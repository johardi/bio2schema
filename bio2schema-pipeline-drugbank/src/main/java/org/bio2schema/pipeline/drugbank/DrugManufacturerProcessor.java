package org.bio2schema.pipeline.drugbank;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonMutators.set;
import static org.bio2schema.util.JsonMutators.with;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static org.bio2schema.vocab.JsonLd.ID;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_MANUFACTURER;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_NAME;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.bio2schema.api.pipeline.Processor;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class DrugManufacturerProcessor implements Processor {

  private final EntityReconciler<GenericEntity> reconciler;

  public DrugManufacturerProcessor(@Nonnull EntityReconciler<GenericEntity> reconciler) {
    this.reconciler = checkNotNull(reconciler);
  }

  @Override
  public JsonNode process(JsonNode input) throws IOException {
    checkIfObjectNode(input);
    JsonNode drugManufacturer = input.path(PROPERTY_MANUFACTURER);
    if (drugManufacturer.isArray()) {
      reconcileDrugManufacturer((ArrayNode) drugManufacturer);
    }
    return input;
  }

  private void reconcileDrugManufacturer(ArrayNode drugManufacturerList) throws IOException {
    for (JsonNode drugManufacturer : drugManufacturerList) {
      checkIfObjectNode(drugManufacturer);
      String manufacturerName = drugManufacturer.get(PROPERTY_NAME).asText();
      Optional<GenericEntity> result = reconciler.reconcile(manufacturerName);
      if (result.isPresent()) {
        GenericEntity organization = result.get();
        set(drugManufacturer, with(ID, organization.getId()));
        set(drugManufacturer, with(PROPERTY_NAME, organization.getName()));
      }
    };
  }
}
