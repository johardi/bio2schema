package org.bio2schema.api.reconciliation;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import com.google.common.cache.CacheBuilder;

public class CachedEntityReconciler<T extends GenericEntity> implements EntityReconciler<T> {

  private static final long DEFAULT_CACHE_SIZE = 100L;

  private final EntityReconciler<T> reconciler;

  private final ConcurrentMap<String, Optional<T>> cachedDatabase;

  public CachedEntityReconciler(@Nonnull EntityReconciler<T> reconciler) {
    this(reconciler, DEFAULT_CACHE_SIZE);
  }

  public CachedEntityReconciler(@Nonnull EntityReconciler<T> reconciler, long cacheSize) {
    this.reconciler = reconciler;
    cachedDatabase = CacheBuilder.newBuilder()
        .maximumSize(cacheSize)
        .<String, Optional<T>>build()
        .asMap();
  }

  @Override
  public Optional<T> reconcile(String inputString) throws IOException {
    Optional<T> result = cachedDatabase.get(inputString);
    if (result == null) {
      result = reconciler.reconcile(inputString);
      cachedDatabase.put(inputString, result);
    }
    return result;
  }
}
