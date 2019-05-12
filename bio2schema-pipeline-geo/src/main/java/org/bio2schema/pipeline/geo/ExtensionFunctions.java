package org.bio2schema.pipeline.geo;

import java.util.List;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import com.schibsted.spt.data.jslt.Function;
import com.schibsted.spt.data.jslt.impl.AbstractCallable;

public class ExtensionFunctions {

  public static Function COMBINE = new Combine();

  private static abstract class AbstractFunction extends AbstractCallable implements Function {
    public AbstractFunction(String name, int min, int max) {
      super(name, min, max);
    }
  }

  // COMBINE FUNCTION
  public static class Combine extends AbstractFunction {

    public Combine() {
      super("combine", 1, 1);
    }

    @Override
    public JsonNode call(JsonNode input, JsonNode[] arguments) {
      ArrayNode array = JacksonUtils.createEmptyArrayNode();
      JsonNode value = arguments[0];
      if (value.isArray()) {
        List<JsonNode> collector = Lists.newArrayList();
        combine(collector, value);
        collector.forEach(i -> array.add(i));
      }
      return array;
    }

    private void combine(List<JsonNode> collector, JsonNode current) {
      for (int i = 0; i < current.size(); i++) {
        JsonNode node = current.get(i);
        if (node.isArray()) {
          combine(collector, node);
        } else {
          if (!node.isNull()) {
            collector.add(node);
          }
        }
      }
    }
  }
}
