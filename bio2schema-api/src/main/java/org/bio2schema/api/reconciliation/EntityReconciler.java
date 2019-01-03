package org.bio2schema.api.reconciliation;

import java.io.IOException;
import java.util.Optional;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;

public interface EntityReconciler<T extends GenericEntity> {

  Optional<T> reconcile(String label) throws IOException;
}
