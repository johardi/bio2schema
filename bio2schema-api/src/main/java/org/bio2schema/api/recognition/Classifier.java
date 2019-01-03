package org.bio2schema.api.recognition;

import java.io.IOException;

public interface Classifier {

  Object classify(String text) throws IOException;
}
