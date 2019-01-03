package org.bio2schema.api.recognition;

import java.io.IOException;
import java.util.Collection;

public interface EntityRecognizer {

  Collection<String> classify(String text) throws IOException;
}
