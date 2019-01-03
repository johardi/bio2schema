package org.bio2schema.pipeline.drugbank;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonMutators.set;
import static org.bio2schema.util.JsonMutators.with;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static org.bio2schema.vocab.JsonLd.ID;
import static org.bio2schema.vocab.JsonLd.TYPE;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CODE;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CODE_VALUE;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CODING_SYSTEM;
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
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DrugNameProcessor implements Processor {

  private final EntityReconciler<MedicalEntity> entityReconciler;

  public DrugNameProcessor(@Nonnull EntityReconciler<MedicalEntity> entityReconciler) {
    this.entityReconciler = checkNotNull(entityReconciler);
  }

  @Override
  public JsonNode process(JsonNode input) throws IOException {
    checkIfObjectNode(input);
    reconcileDrug(input);
    return input;
  }

  private void reconcileDrug(JsonNode drug) throws IOException {
    JsonNode drugName = drug.path(PROPERTY_NAME);
    if (drugName.isTextual()) {
      Optional<MedicalEntity> result = entityReconciler.reconcile(drugName.asText());
      if (result.isPresent()) {
        MedicalEntity medicalEntity = result.get();
        Optional<MedicalCode> medicalCode = medicalEntity.getMedicalCode();
        if (medicalCode.isPresent()) {
          ObjectNode code = JacksonUtils.createEmptyObjectNode();
          set(code, with(TYPE, TYPE_MEDICAL_CODE));
          set(code, with(ID, medicalCode.get().getCui()));
          set(code, with(PROPERTY_CODE_VALUE, medicalCode.get().getCodeValue()));
          set(code, with(PROPERTY_CODING_SYSTEM, medicalCode.get().getCodingSystem()));
          set(drug, with(PROPERTY_CODE, code));
        }
      }
    }
  }
}
