package org.bio2schema.pipeline.clinicaltrials;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonMutators.append;
import static org.bio2schema.util.JsonMutators.set;
import static org.bio2schema.util.JsonMutators.with;
import static org.bio2schema.util.JsonPreconditions.checkIfArrayNode;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static org.bio2schema.vocab.JsonLd.ID;
import static org.bio2schema.vocab.JsonLd.TYPE;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_ALTERNATE_NAME;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CODE;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CODE_VALUE;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_CODING_SYSTEM;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_NAME;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_STUDY_SUBJECT;
import static org.bio2schema.vocab.SchemaOrg.TYPE_MEDICAL_CODE;
import java.io.IOException;
import java.util.List;
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
import com.google.common.collect.Lists;

public final class StudySubjectProcessor implements Processor {

  private final List<Processor> subProcessors = Lists.newArrayList();

  public StudySubjectProcessor addSubProcessor(@Nonnull Processor processor) {
    subProcessors.add(checkNotNull(processor));
    return this;
  }

  @Override
  public JsonNode process(JsonNode input) throws IOException {
    checkIfObjectNode(input);
    JsonNode studySubject = input.path(PROPERTY_STUDY_SUBJECT);
    if (studySubject.isArray()) {
      for (Processor p : subProcessors) {
        p.process(studySubject);
      }
    }
    return input;
  }

  public static class ReconcileMedicalTerm implements Processor {

    private final EntityReconciler<MedicalEntity> reconciler;

    public ReconcileMedicalTerm(@Nonnull EntityReconciler<MedicalEntity> reconciler) {
      this.reconciler = checkNotNull(reconciler);
    }

    @Override
    public JsonNode process(JsonNode input) throws IOException {
      checkIfArrayNode(input);
      for (JsonNode studySubject : (ArrayNode) input) {
        checkIfObjectNode(studySubject);
        String subjectName = studySubject.get(PROPERTY_NAME).asText();
        Optional<MedicalEntity> result = reconciler.reconcile(subjectName);
        if (result.isPresent()) {
          MedicalEntity medicalEntity = result.get();
          set(studySubject, with(ID, medicalEntity.getId()));
          set(studySubject, with(TYPE, medicalEntity.getType()));
          set(studySubject, with(PROPERTY_NAME, medicalEntity.getName()));
          set(studySubject, with(PROPERTY_ALTERNATE_NAME, medicalEntity.getSynonyms()));
          Optional<MedicalCode> medicalCode = medicalEntity.getMedicalCode();
          if (medicalCode.isPresent()) {
            ObjectNode code = JacksonUtils.createEmptyObjectNode();
            set(code, with(TYPE, TYPE_MEDICAL_CODE));
            set(code, with(ID, medicalCode.get().getCui()));
            set(code, with(PROPERTY_CODE_VALUE, medicalCode.get().getCodeValue()));
            set(code, with(PROPERTY_CODING_SYSTEM, medicalCode.get().getCodingSystem()));
            set(studySubject, with(PROPERTY_CODE, code));
          }
        }
      }
      return input;
    }
  }

  public static class ReconcileDrugTerm implements Processor {

    private final EntityReconciler<MedicalEntity> reconciler;

    public ReconcileDrugTerm(@Nonnull EntityReconciler<MedicalEntity> reconciler) {
      this.reconciler = checkNotNull(reconciler);
    }

    @Override
    public JsonNode process(JsonNode input) throws IOException {
      checkIfArrayNode(input);
      for (JsonNode studySubject : (ArrayNode) input) {
        checkIfObjectNode(studySubject);
        String subjectName = studySubject.get(PROPERTY_NAME).asText();
        Optional<MedicalEntity> result = reconciler.reconcile(subjectName);
        if (result.isPresent()) {
          MedicalEntity medicalEntity = result.get();
          set(studySubject, with(ID, medicalEntity.getId()));
          set(studySubject, with(TYPE, medicalEntity.getType()));
          set(studySubject, with(PROPERTY_NAME, medicalEntity.getName()));
          append(studySubject, with(PROPERTY_ALTERNATE_NAME, medicalEntity.getSynonyms()));
        }
      }
      return input;
    }
  }
}
