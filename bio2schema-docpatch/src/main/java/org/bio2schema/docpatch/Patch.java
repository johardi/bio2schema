package org.bio2schema.docpatch;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

public class Patch {

  private final JsonNode patchNode;

  public Patch(@Nonnull JsonNode patchNode) {
    this.patchNode = checkNotNull(patchNode);
  }

  public JsonNode getJson() {
    return patchNode;
  }

  public JsonNode apply(JsonNode document) throws IOException {
    JsonPatch patch = createPatch();
    try {
      return patch.apply(document);
    } catch (JsonPatchException e) {
      throw new IOException(e);
    }
  }

  private JsonPatch createPatch() throws IOException {
    JsonNode collectionOfPatches = JacksonUtils.createEmptyArrayNode().add(patchNode);
    return JsonPatch.fromJson(collectionOfPatches);
  }

  @Override
  public String toString() {
    return JacksonUtils.prettyPrint(patchNode);
  }
}
