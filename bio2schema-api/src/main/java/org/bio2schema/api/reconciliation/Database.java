package org.bio2schema.api.reconciliation;

import java.io.IOException;

public interface Database {

  String getName();

  Object find(String label) throws IOException;
}
