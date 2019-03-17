package org.bio2schema.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Charsets;

public final class JacksonUtils {

  public static final ObjectMapper mapper = new ObjectMapper();

  public static ObjectMapper getMapper() {
    return mapper;
  }

  public static ObjectNode createEmptyObjectNode() {
    return mapper.createObjectNode();
  }

  public static ArrayNode createEmptyArrayNode() {
    return mapper.createArrayNode();
  }

  public static String prettyPrint(InputStream is) {
    try {
      return prettyPrint(mapper.readTree(is));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void prettyPrint(InputStream is, OutputStream out) {
    try {
      prettyPrint(mapper.readTree(is), out);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String prettyPrint(JsonNode json) {
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void prettyPrint(JsonNode json, OutputStream out) {
    try {
      String jsonString = prettyPrint(json);
      out.write(jsonString.getBytes(Charsets.UTF_8));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static JsonNode readJson(Reader r) throws IOException {
    return mapper.readValue(r, JsonNode.class);
  }

  /*
   * Utility methods for working with XML format
   */

  private static final XmlMapper xmlMapper = XmlMapperFactory.createMapper();

  public static JsonNode readXmlAsJson(Reader r) throws IOException {
    Object xmlTreeObject = xmlMapper.readValue(r, Object.class);
    return mapper.valueToTree(xmlTreeObject);
  }

  public static JsonNode readXmlAsJson(String xml) throws IOException {
    Object xmlTreeObject = xmlMapper.readValue(xml, Object.class);
    return mapper.valueToTree(xmlTreeObject);
  }

  /*
   * Utility methods for working with CSV format
   */

  private static final CsvMapper csvMapper = new CsvMapper();

  public static JsonNode readCsvAsJson(Reader r) throws IOException {
    final Iterator<JsonNode> it = csvMapper.readerFor(JsonNode.class)
        .with(CsvSchema.emptySchema().withHeader())
        .readValues(r);
    return constructArrayNode(it);
  }

  public static JsonNode readCsvAsJson(String csv) throws IOException {
    final Iterator<JsonNode> it = csvMapper.readerFor(JsonNode.class)
        .with(CsvSchema.emptySchema().withHeader())
        .readValues(csv);
    return constructArrayNode(it);
  }

  private static JsonNode constructArrayNode(final Iterator<JsonNode> it) {
    ArrayNode arrayNode = createEmptyArrayNode();
    while (it.hasNext()) {
      arrayNode.add(it.next());
    }
    return arrayNode;
  }
}
