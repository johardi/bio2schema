package org.bio2schema.pipeline.pubmed;

import org.bio2schema.api.pipeline.Transformer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Parser;

public class MedicalScholarlyArticleTransformer implements Transformer {

  public static final String SOURCE_JSLT_RESOURCE = "pubmed.jslt";

  private final Expression jsltExpression;

  public MedicalScholarlyArticleTransformer() {
    jsltExpression = Parser.compileResource(SOURCE_JSLT_RESOURCE,
        Sets.newHashSet(
            ExtensionFunctions.UNIQUE,
            ExtensionFunctions.REFORMAT_DATE));
  }

  @Override
  public JsonNode transform(JsonNode jsonObject) {
    return jsltExpression.apply(jsonObject);
  }
}
