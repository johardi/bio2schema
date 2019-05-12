package org.bio2schema.pipeline.geo;

import java.io.IOException;
import org.bio2schema.api.pipeline.Transformer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Parser;

public class DataCatalogTransformer  implements Transformer {

  public static final String SOURCE_JSLT_RESOURCE = "geo.jslt";

  private final Expression jsltExpression;

  public DataCatalogTransformer() {
    jsltExpression = Parser.compileResource(SOURCE_JSLT_RESOURCE,
        Sets.newHashSet(
            ExtensionFunctions.COMBINE));
  }

  @Override
  public JsonNode transform(JsonNode jsonObject) throws IOException {
    return jsltExpression.apply(jsonObject);
  }
}
