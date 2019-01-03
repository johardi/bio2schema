package org.bio2schema.api.reconciliation;

import java.io.IOException;
import java.util.Map;
import javax.annotation.Nullable;

public abstract class LocalDatabase implements Database {

  @Nullable
  @Override
  public Object find(String inputString) throws IOException {
    return getDirectoryMap().get(inputString);
  }

  protected abstract Map<String, Object> getDirectoryMap();
}
