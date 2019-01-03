package bio2schema.reconciler.drugbank;

import static org.bio2schema.util.JsonPreconditions.checkIfArrayNode;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import org.bio2schema.api.reconciliation.LocalDatabase;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DrugBankDatabase extends LocalDatabase {

  private static final String DEFAULT_DB_SOURCE = "drugbank.csv";

  @Override
  protected Map<String, Object> getDirectoryMap() {
    final ClassLoader cl = DrugBankDatabase.class.getClassLoader();
    try (InputStream stream = cl.getResourceAsStream(DEFAULT_DB_SOURCE)) {
      JsonNode csvDatabase = JacksonUtils.readCsvAsJson(new InputStreamReader(stream, Charsets.UTF_8));
      return createDirectoryMap(csvDatabase);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Map<String, Object> createDirectoryMap(JsonNode node) {
    checkIfArrayNode(node);
    final Map<String, Object> directoryMap = Maps.newHashMap();
    for (JsonNode record : (ArrayNode) node) {
      directoryMap.put(record.get("Common name").asText(), record);
      String synonyms = record.get("Synonyms").asText();
      Lists.newArrayList(synonyms.split(" \\| ")).forEach(s -> directoryMap.put(s, record));
    }
    return directoryMap;
  }
}
