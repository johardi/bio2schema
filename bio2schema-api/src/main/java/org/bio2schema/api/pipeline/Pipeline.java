package org.bio2schema.api.pipeline;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import javax.annotation.Nonnull;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

public class Pipeline {

  private final Transformer transformer;

  private final List<Processor> listOfProcessors;

  public Pipeline(@Nonnull Transformer transformer, @Nonnull Processor... processors) {
    this.transformer = checkNotNull(transformer);
    this.listOfProcessors = Lists.newArrayList(checkNotNull(processors));
  }

  public JsonNode process(JsonNode json) throws IOException {
    JsonNode output = transformer.transform(json);
    for (Processor p : listOfProcessors) {
      output = p.process(output);
    }
    return output;
  }

  public JsonNode ingestFromFile(File file) throws IOException {
    JsonNode json = readAsJson(file);
    return process(json);
  }

  private JsonNode readAsJson(File file) throws IOException {
    String mimeType = Files.probeContentType(file.toPath());
    switch (mimeType) {
      case "application/json":
        return JacksonUtils.mapper.readTree(reader(file));
      case "application/xml":
      case "text/xml":
        return JacksonUtils.readXmlAsJson(reader(file));
      case "text/csv":
        return JacksonUtils.readCsvAsJson(reader(file));
      default:
        throw new IOException("Unsupported file type: " + mimeType);
    }
  }

  private static Reader reader(File file) throws IOException {
    return new FileReader(file, StandardCharsets.UTF_8);
  }
}