package org.bio2schema.util;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ShortNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.Sets;

public final class JsonMutators {

  public static void set(JsonNode parent, WithExpression expr) {
    checkIfObjectNode(parent);
    set((ObjectNode) parent, expr);
  }

  public static void set(ObjectNode parent, WithExpression expr) {
    if (expr.value == null) {
      return;
    }
    parent.set(expr.key, expr.value);
  }

  public static void append(JsonNode parent, WithExpression expr) {
    checkIfObjectNode(parent);
    append((ObjectNode) parent, expr);
  }

  public static void append(ObjectNode parent, WithExpression expr) {
    if (expr.value == null) {
      return;
    }
    JsonNode foundNode = parent.path(expr.key);
    if (foundNode.isMissingNode()) {
      set(parent, expr);
    } else {
      if (foundNode instanceof ArrayNode) {
        ArrayNode arrayNode = (ArrayNode) foundNode;
        arrayNode.add(expr.value);
        flatten(arrayNode);
      } else {
        ArrayNode arrayNode = JacksonUtils.createEmptyArrayNode();
        arrayNode.add(foundNode);
        arrayNode.add(expr.value);
        flatten(arrayNode);
        parent.set(expr.key, arrayNode);
      }
    }
  }

  private static void flatten(final ArrayNode arrayNode) {
    Set<JsonNode> collector = Sets.newHashSet();
    unique(collector, arrayNode);
    arrayNode.removeAll();
    collector.forEach(i -> arrayNode.add(i));
  }
  
  private static void unique(Set<JsonNode> collector, JsonNode current) {
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

  public static WithExpression with(String key, JsonNode value) {
    return new WithExpression(key, value);
  }

  public static WithExpression with(String key, String value) {
    return with(key, TextNode.valueOf(value));
  }

  public static WithExpression with(String key, long value) {
    return with(key, LongNode.valueOf(value));
  }

  public static WithExpression with(String key, int value) {
    return with(key, IntNode.valueOf(value));
  }

  public static WithExpression with(String key, short value) {
    return with(key, ShortNode.valueOf(value));
  }

  public static WithExpression with(String key, double value) {
    return with(key, DoubleNode.valueOf(value));
  }

  public static WithExpression with(String key, float value) {
    return with(key, FloatNode.valueOf(value));
  }

  public static WithExpression with(String key, boolean value) {
    return with(key, BooleanNode.valueOf(value));
  }

  public static WithExpression with(String key, Collection<String> values) {
    return with(key, StringArrayNode.valueOf(values));
  }

  private static class WithExpression {

    private final String key;
    @Nullable private final JsonNode value;

    private WithExpression(@Nonnull String key, @Nullable JsonNode value) {
      this.key = checkNotNull(key);
      this.value = value;
    }
  }

  private static class StringArrayNode {

    @Nullable
    private static ArrayNode valueOf(Collection<String> values) {
      if (values == null) {
        return null;
      }
      ArrayNode arrayNode = JacksonUtils.createEmptyArrayNode();
      values.forEach(v -> arrayNode.add(v));
      return arrayNode;
    }
  }
}
