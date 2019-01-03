package org.bio2schema.api.reconciliation;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import com.google.common.cache.CacheBuilder;

public abstract class CachedEntityReconciler<T extends GenericEntity>
    extends StandardEntityReconciler<T> {

  private final ConcurrentMap<String, Optional<T>> cachedDatabase =
      CacheBuilder.newBuilder().maximumSize(100L).<String, Optional<T>>build().asMap();

  public CachedEntityReconciler(@Nonnull String serviceName, @Nonnull Database database) {
    super(serviceName, database);
  }

  @Override
  public Optional<T> reconcile(String inputString) throws IOException {
    try {
      Optional<T> result = cachedDatabase.get(inputString);
      if (result == null) {
        result = retrieveFromDatabase(inputString);
        cachedDatabase.put(inputString, result);
      }
      return result;
    } catch (IOException e) {
      String message = String.format(
          "A problem occurred using reconciler '%s'\n> %s",
          serviceName, e.getMessage());
      throw new IOException(message);
    }
  }

  private Optional<T> retrieveFromDatabase(String inputString) throws IOException {
    Object outputResult = database.find(inputString);
    return Optional.ofNullable(handleResult(inputString, outputResult));
  }
}
