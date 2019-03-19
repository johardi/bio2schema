package org.bio2schema.docpatch;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.bio2schema.api.pipeline.Processor;
import com.fasterxml.jackson.databind.JsonNode;

public class PatchModifier {

  private final Processor processor;

  public PatchModifier(@Nonnull Processor processor) {
    this.processor = checkNotNull(processor);
  }

  public Patch changeValue(Patch patch) throws IOException {
    JsonNode patchNode = patch.getJson();
    JsonNode newPatchNode = processor.process(patchNode);
    return new Patch(newPatchNode);
  }
}
