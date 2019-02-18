package org.bio2schema;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.File;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bio2schema.api.pipeline.Pipeline;
import com.fasterxml.jackson.databind.JsonNode;

public class PipelineExecutor {

  private static Logger logger = LogManager.getRootLogger();

  private final Pipeline pipeline;
  
  public PipelineExecutor(@Nonnull Pipeline pipeline) {
    this.pipeline = checkNotNull(pipeline);
  }

  public ResultObject submit(File document) {
    try {
      logger.info("Processing document: [{}]", document.getName());
      JsonNode outputJson = pipeline.ingestFromFile(document);
      return new ResultObject() {
        // @formatter: off
        @Override public Path getInputLocation() { return document.toPath(); }
        @Override public JsonNode getContent() { return outputJson; }
        // @formatter: on
      };
    } catch (Exception e) {
      return new ResultObject() {
        // @formatter: off
        @Override public Path getInputLocation() { return document.toPath(); }
        @Override public JsonNode getContent() throws Exception { throw e; }
        // @formatter: on
      };
    }
  }
}
