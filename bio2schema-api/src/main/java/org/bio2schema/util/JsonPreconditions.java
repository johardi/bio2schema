package org.bio2schema.util;

import javax.annotation.Nullable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.Strings;

public final class JsonPreconditions {

  private static final String NOT_JSON_NODE_DEFAULT_MESSAGE = "Object is not a JSON node";
  private static final String MUST_JSON_OBJECT_DEFAULT_MESSAGE = "Value must be a JSON object";
  private static final String MUST_JSON_ARRAY_DEFAULT_MESSAGE = "Value must be a JSON array";
  private static final String MUST_JSON_TEXT_DEFAULT_MESSAGE = "Value must be a text";
  private static final String MUST_JSON_NUMBER_DEFAULT_MESSAGE = "Value must be a number";
  private static final String MUST_JSON_BOOLEAN_DEFAULT_MESSAGE = "Value must be a boolean value";

  public static ObjectNode checkIfObjectNode(Object o) {
    return checkIfObjectNode(o, null);
  }

  public static ObjectNode checkIfObjectNode(Object o, @Nullable String errorMessage) {
    return checkIfObjectNode(checkIfJsonNode(o, errorMessage), errorMessage);
  }

  public static ArrayNode checkIfArrayNode(Object o) {
    return checkIfArrayNode(o, null);
  }

  public static ArrayNode checkIfArrayNode(Object o, @Nullable String errorMessage) {
    return checkIfArrayNode(checkIfJsonNode(o, errorMessage), errorMessage);
  }

  public static TextNode checkIfTextNode(Object o) {
    return checkIfTextNode(o, null);
  }

  public static TextNode checkIfTextNode(Object o, @Nullable String errorMessage) {
    return checkIfTextNode(checkIfJsonNode(o, errorMessage), errorMessage);
  }

  public static NumericNode checkIfNumericNode(Object o) {
    return checkIfNumericNode(o, null);
  }

  public static NumericNode checkIfNumericNode(Object o, @Nullable String errorMessage) {
    return checkIfNumericNode(checkIfJsonNode(o, errorMessage), errorMessage);
  }

  public static BooleanNode checkIfBooleanNode(Object o) {
    return checkIfBooleanNode(o, null);
  }

  public static BooleanNode checkIfBooleanNode(Object o, @Nullable String errorMessage) {
    return checkIfBooleanNode(checkIfJsonNode(o, errorMessage), errorMessage);
  }

  public static JsonNode checkIfJsonNode(Object o) {
    return checkIfJsonNode(o, null);
  }

  public static JsonNode checkIfJsonNode(Object o, String errorMessage) {
    if (!(o instanceof JsonNode)) {
      throw new IllegalArgumentException(
          produceMessage(errorMessage, NOT_JSON_NODE_DEFAULT_MESSAGE));
    }
    return (JsonNode) o;
  }

  public static ObjectNode checkIfObjectNode(JsonNode node) {
    return checkIfObjectNode(node, null);
  }

  public static ObjectNode checkIfObjectNode(JsonNode node, @Nullable String errorMessage) {
    if (!(node instanceof ObjectNode)) {
      throw new IllegalArgumentException(
          produceMessage(errorMessage, MUST_JSON_OBJECT_DEFAULT_MESSAGE));
    }
    return (ObjectNode) node;
  }

  public static ArrayNode checkIfArrayNode(JsonNode node) {
    return checkIfArrayNode(node, null);
  }

  public static ArrayNode checkIfArrayNode(JsonNode node, @Nullable String errorMessage) {
    if (!(node instanceof ArrayNode)) {
      throw new IllegalArgumentException(
          produceMessage(errorMessage, MUST_JSON_ARRAY_DEFAULT_MESSAGE));
    }
    return (ArrayNode) node;
  }

  public static TextNode checkIfTextNode(JsonNode node) {
    return checkIfTextNode(node, null);
  }

  public static TextNode checkIfTextNode(JsonNode node, @Nullable String errorMessage) {
    if (!(node instanceof TextNode)) {
      throw new IllegalArgumentException(
          produceMessage(errorMessage, MUST_JSON_TEXT_DEFAULT_MESSAGE));
    }
    return (TextNode) node;
  }

  public static NumericNode checkIfNumericNode(JsonNode node) {
    return checkIfNumericNode(node, null);
  }

  public static NumericNode checkIfNumericNode(JsonNode node, @Nullable String errorMessage) {
    if (!(node instanceof NumericNode)) {
      throw new IllegalArgumentException(
          produceMessage(errorMessage, MUST_JSON_NUMBER_DEFAULT_MESSAGE));
    }
    return (NumericNode) node;
  }

  public static BooleanNode checkIfBooleanNode(JsonNode node) {
    return checkIfBooleanNode(node, null);
  }

  public static BooleanNode checkIfBooleanNode(JsonNode node, @Nullable String errorMessage) {
    if (!(node instanceof BooleanNode)) {
      throw new IllegalArgumentException(
          produceMessage(errorMessage, MUST_JSON_BOOLEAN_DEFAULT_MESSAGE));
    }
    return (BooleanNode) node;
  }

  private static String produceMessage(@Nullable String userErrorMessage, String defaultMessage) {
    return Strings.isNullOrEmpty(userErrorMessage) ? defaultMessage : userErrorMessage;
  }
}
