package org.bio2schema.pipeline.drugbank;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.bio2schema.api.pipeline.MultiProcessor;
import org.bio2schema.api.pipeline.Processor;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity.MedicalCode;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import static org.bio2schema.util.JsonMutators.set;
import static org.bio2schema.util.JsonMutators.with;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static org.bio2schema.vocab.JsonLd.ID;
import static org.bio2schema.vocab.JsonLd.TYPE;
import static org.bio2schema.vocab.SchemaOrg.*;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CODE_VALUE;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CODING_SYSTEM;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_NAME;
import static org.bio2schema.vocab.SchemaOrg.TYPE_MEDICAL_CODE;

public class DrugNameProcessor implements MultiProcessor {

  private final List<Processor> subProcessors = Lists.newArrayList();

  public DrugNameProcessor addSubProcessor(@Nonnull Processor processor) {
    registerProcessor(processor);
    return this;
  }

  @Override
  public void registerProcessor(@Nonnull Processor processor) {
    subProcessors.add(checkNotNull(processor));
  }

  @Override
  public JsonNode process(JsonNode input) throws IOException {
    checkIfObjectNode(input);
    for (Processor p : subProcessors) {
      p.process(input);
    }
    return input;
  }

  public static class ReconcileDrugTerm implements Processor {

    private final EntityReconciler<MedicalEntity> reconciler;

    public ReconcileDrugTerm(@Nonnull EntityReconciler<MedicalEntity> reconciler) {
      this.reconciler = checkNotNull(reconciler);
    }

    @Override
    public JsonNode process(JsonNode drug) throws IOException {
      JsonNode drugName = drug.path(PROPERTY_NAME);
      if (drugName.isTextual()) {
        Optional<MedicalEntity> result = reconciler.reconcile(drugName.asText());
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
      return drug;
    }
  }

  public static class ReconcileDrugRxCui implements Processor {

    private final EntityReconciler<MedicalEntity> reconciler;

    public ReconcileDrugRxCui(@Nonnull EntityReconciler<MedicalEntity> reconciler) {
      this.reconciler = checkNotNull(reconciler);
    }

    @Override
    public JsonNode process(JsonNode drug) throws IOException {
      JsonNode drugName = drug.path(PROPERTY_NAME);
      if (drugName.isTextual()) {
        Optional<MedicalEntity> result = reconciler.reconcile(drugName.asText());
        if (result.isPresent()) {
          MedicalEntity medicalEntity = result.get();
          Optional<MedicalCode> medicalCode = medicalEntity.getMedicalCode();
          if (medicalCode.isPresent()) {
            set(drug, with(PROPERTY_RXCUI, medicalCode.get().getCodeValue()));
          }
        }
      }
      return drug;
    }
  }
}
