package org.bio2schema.api.pipeline;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

public class Pipeline {

  private final Transformer transformer;

  private final List<Processor> listOfProcessors;

  public Pipeline(@Nonnull Transformer transformer, @Nonnull Processor... processors) {
    this.transformer = checkNotNull(transformer);
    this.listOfProcessors = Lists.newArrayList(checkNotNull(processors));
  }

  public JsonNode process(JsonNode json) throws IOException {
    JsonNode output = transformer.transform(json);
    for (Processor p : listOfProcessors) {
      output = p.process(output);
    }
    return output;
  }
}