package org.bio2schema.docpatch;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import javax.annotation.Nonnull;
import com.fasterxml.jackson.databind.JsonNode;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Parser;

public class PatchTemplate {

  private final Expression expression;

  // Avoid instantiation
  private PatchTemplate(@Nonnull Expression expression) {
    this.expression = checkNotNull(expression);
  }

  public static PatchTemplate readValue(@Nonnull Reader r) {
    return new PatchTemplate(Parser.compile("jsonpatch", r, Collections.emptySet()));
  }

  public Patch compile(JsonNode node) throws IOException {
    JsonNode patchNode = expression.apply(node);
    return new Patch(patchNode);
  }
}
