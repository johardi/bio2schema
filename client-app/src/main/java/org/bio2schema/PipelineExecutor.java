package org.bio2schema;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.bio2schema.api.pipeline.Pipeline;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;

public class PipelineExecutor {

  private final Pipeline pipeline;
  
  public PipelineExecutor(@Nonnull Pipeline pipeline) {
    this.pipeline = checkNotNull(pipeline);
  }

  interface ResultBundle {
    Path getSourceInput();
    JsonNode getContent() throws Exception;
  }

  public ResultBundle submit(Path inputLocation) {
    try {
      Reader reader = new FileReader(inputLocation.toFile(), StandardCharsets.UTF_8);
      JsonNode inputJson = JacksonUtils.readXmlAsJson(reader);
      JsonNode outputJson = pipeline.process(inputJson);
      return new ResultBundle() {
        // @formatter: off
        @Override public Path getSourceInput() { return inputLocation; }
        @Override public JsonNode getContent() { return outputJson; }
        // @formatter: on
      };
    } catch (Exception e) {
      return new ResultBundle() {
        // @formatter: off
        @Override public Path getSourceInput() { return inputLocation; }
        @Override public JsonNode getContent() throws Exception { throw e; }
        // @formatter: on
      };
    }
  }
}
