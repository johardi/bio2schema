package org.bio2schema.api.reconciliation.entitytype;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

public interface MedicalEntity extends GenericEntity {

  Optional<MedicalCode> getMedicalCode();
  Collection<String> getSynonyms();

  public static interface MedicalCode extends Serializable {
    String getCui();
    String getCodeValue();
    String getCodingSystem();
  }
}
