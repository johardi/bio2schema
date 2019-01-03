package org.bio2schema.pipeline.drugbank;

import java.util.Set;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Sets;
import com.schibsted.spt.data.jslt.Function;
import com.schibsted.spt.data.jslt.impl.AbstractCallable;

public class ExtensionFunctions {

  public static Function UNIQUE = new Unique();

  private static abstract class AbstractFunction extends AbstractCallable implements Function {
    public AbstractFunction(String name, int min, int max) {
      super(name, min, max);
    }
  }

  // UNIQUE FUNCTION
  public static class Unique extends AbstractFunction {

    public Unique() {
      super("unique", 1, 1);
    }

    @Override
    public JsonNode call(JsonNode input, JsonNode[] arguments) {
      ArrayNode array = JacksonUtils.mapper.createArrayNode();
      JsonNode value = arguments[0];
      if (value.isArray()) {
        Set<JsonNode> collector = Sets.newHashSet();
        unique(collector, value);
        collector.forEach(i -> array.add(i));
      }
      return array;
    }

    private void unique(Set<JsonNode> collector, JsonNode current) {
      for (int i = 0; i < current.size(); i++) {
        JsonNode node = current.get(i);
        if (node.isArray()) {
          unique(collector, node);
        } else {
          if (!node.isNull()) {
            collector.add(node);
          }
        }
      }
    }
  }
}
