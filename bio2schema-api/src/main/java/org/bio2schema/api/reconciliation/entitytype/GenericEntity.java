package org.bio2schema.api.reconciliation.entitytype;

import java.io.Serializable;
import java.util.Map;

public interface GenericEntity extends Serializable {

  String getId();
  String getType();
  String getName();
  String getDescription();
  Map<String, Object> getAdditionalProperties();
}
