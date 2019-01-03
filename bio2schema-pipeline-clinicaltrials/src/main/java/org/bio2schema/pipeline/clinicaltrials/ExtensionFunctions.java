package org.bio2schema.pipeline.clinicaltrials;

import java.util.Set;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.Sets;
import com.schibsted.spt.data.jslt.Function;
import com.schibsted.spt.data.jslt.impl.AbstractCallable;

public class ExtensionFunctions {

  public static Function UNWRAP = new Unwrap();
  public static Function UNIQUE = new Unique();

  private static abstract class AbstractFunction extends AbstractCallable implements Function {
    public AbstractFunction(String name, int min, int max) {
      super(name, min, max);
    }
  }

  // UNWRAP FUNCTION
  public static class Unwrap extends AbstractFunction {
    
    public Unwrap() {
      super("unwrap", 1, 1);
    }

    @Override
    public JsonNode call(JsonNode input, JsonNode[] arguments) {
      JsonNode arg = arguments[0];
      if (arg.isTextual()) {
        String text = unwrap(arg.asText());
        return new TextNode(text);
      } else if (arg.isArray()) {
        String text = unwrap(arg.get(0).asText());
        return new TextNode(text);
      }
      return NullNode.getInstance();
    }

    private String unwrap(String text) {
      return text.replaceAll("\n{2}\\h{2,}", "\n\n").replaceAll("\n\\h{2,}", " ").trim();
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
