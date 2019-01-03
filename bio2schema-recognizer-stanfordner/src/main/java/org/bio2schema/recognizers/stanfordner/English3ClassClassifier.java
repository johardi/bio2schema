package org.bio2schema.recognizers.stanfordner;

import java.io.IOException;
import org.bio2schema.api.recognition.Classifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class English3ClassClassifier implements Classifier {

  public enum Type {
    PERSON("PERSON"),
    ORGANIZATION("ORGANIZATION"),
    LOCATION("LOCATION");

    private final String name;
    Type(String name) { this.name = name; }
    public String getString() { return name; }
  }

  private static final String SOURCE_CLASSIFIER = "english.all.3class.distsim.crf.ser.gz";

  private static final CRFClassifier<CoreLabel> classifier;
  static {
    try {
      classifier = CRFClassifier.getClassifier(SOURCE_CLASSIFIER);
    } catch (ClassCastException | ClassNotFoundException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Object classify(String text) throws IOException {
    return classifier.classifyWithInlineXML(text);
  }
}
