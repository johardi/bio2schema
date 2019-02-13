package org.bio2schema;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bio2schema.api.pipeline.Pipeline;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;

public class PipelineExecutor {

  private static Logger logger = LogManager.getRootLogger();

  private final Pipeline pipeline;
  
  public PipelineExecutor(@Nonnull Pipeline pipeline) {
    this.pipeline = checkNotNull(pipeline);
  }

  public ResultObject submit(Path inputLocation) {
    try {
      logger.info("Processing document: [{}]", inputLocation.getFileName());
      Reader reader = new FileReader(inputLocation.toFile(), StandardCharsets.UTF_8);
      JsonNode inputJson = JacksonUtils.readXmlAsJson(reader);
      JsonNode outputJson = pipeline.process(inputJson);
      return new ResultObject() {
        // @formatter: off
        @Override public Path getInputLocation() { return inputLocation; }
        @Override public JsonNode getContent() { return outputJson; }
        // @formatter: on
      };
    } catch (Exception e) {
      return new ResultObject() {
        // @formatter: off
        @Override public Path getInputLocation() { return inputLocation; }
        @Override public JsonNode getContent() throws Exception { throw e; }
        // @formatter: on
      };
    }
  }
}
