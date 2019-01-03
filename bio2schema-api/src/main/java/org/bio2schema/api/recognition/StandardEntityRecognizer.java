package org.bio2schema.api.recognition;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.util.Collection;
import javax.annotation.Nonnull;

public abstract class StandardEntityRecognizer implements EntityRecognizer {

  private final String serviceName;
  private final Classifier classifier;

  public StandardEntityRecognizer(@Nonnull String serviceName, @Nonnull Classifier classifier) {
    this.serviceName = checkNotNull(serviceName);
    this.classifier = checkNotNull(classifier);
  }

  @Override
  public Collection<String> classify(String text) throws IOException {
    try {
      Object result = classifier.classify(text);
      return handleResult(result);
    } catch (IOException e) {
      String message = String.format(
          "A problem occurred using recognizer '%s'\n> %s",
          serviceName, e.getMessage());
      throw new IOException(message);
    }
  }

  protected abstract Collection<String> handleResult(Object result) throws IOException;
}
