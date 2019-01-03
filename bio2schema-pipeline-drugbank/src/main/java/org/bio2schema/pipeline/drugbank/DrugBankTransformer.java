package org.bio2schema.pipeline.drugbank;

import org.bio2schema.api.pipeline.Transformer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Parser;

public class DrugBankTransformer implements Transformer {

  public static final String SOURCE_JSLT_RESOURCE = "drugbank.jslt";

  private final Expression jsltExpression;

  public DrugBankTransformer() {
    jsltExpression = Parser.compileResource(SOURCE_JSLT_RESOURCE,
        Sets.newHashSet(
            ExtensionFunctions.UNIQUE));
  }

  @Override
  public JsonNode transform(JsonNode json) {
    return jsltExpression.apply(json);
  }
}
