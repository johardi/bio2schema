package org.bio2schema.recognizers.stanfordner;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Collection;
import javax.annotation.Nonnull;
import org.bio2schema.api.recognition.Classifier;
import org.bio2schema.api.recognition.StandardEntityRecognizer;

public class StanfordNer extends StandardEntityRecognizer {

  private String entityType;

  public StanfordNer(@Nonnull Classifier classifier, @Nonnull String entityType) {
    super("Stanford NER", classifier);
    this.entityType = checkNotNull(entityType);
  }

  @Override
  public Collection<String> handleResult(final Object result) {
    return new XmlResultWrapper(result.toString()).getAllStringsEnclosedBy(entityType);
  }
}