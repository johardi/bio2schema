package org.bio2schema.api.reconciliation;

import java.io.IOException;

public interface Database {

  Object find(String label) throws IOException;
}
