package org.bio2schema.api.reconciliation;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;

public abstract class StandardEntityReconciler<T extends GenericEntity> implements EntityReconciler<T> {

  private final String serviceName;
  private final Database database;

  public StandardEntityReconciler(@Nonnull String serviceName, @Nonnull Database database) {
    this.serviceName = checkNotNull(serviceName);
    this.database = checkNotNull(database);
  }

  @Override
  public Optional<T> reconcile(String inputString) throws IOException {
    try {
      Object outputResult = database.find(inputString);
      return Optional.ofNullable(handleResult(inputString, outputResult));
    } catch (IOException e) {
      String message = String.format(
          "A problem occurred using reconciler '%s'\n> %s",
          serviceName, e.getMessage());
      throw new IOException(message);
    }
  }

  @Nullable
  protected abstract T handleResult(String inputString, Object outputResult) throws IOException;
}
