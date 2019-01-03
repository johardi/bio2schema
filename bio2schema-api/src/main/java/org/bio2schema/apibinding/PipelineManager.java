package org.bio2schema.apibinding;

import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bio2schema.api.pipeline.Pipeline;
import org.bio2schema.api.pipeline.PipelineFactory;

public class PipelineManager {

  private static Logger logger = LogManager.getRootLogger();

  private Set<PipelineFactory> pipelineFactories;

  public Optional<Pipeline> getPipelineFor(String datasetName) {
    for (PipelineFactory factory : pipelineFactories) {
      if (factory.isFactoryFor(datasetName)) {
        return Optional.of(factory.createPipeline());
      }
    }
    logger.warn(String.format(
        "A problem occurred finding a pipeline for '%s' dataset",
        datasetName));
    return Optional.empty();
  }

  @Inject
  private void setPipelineFactories(Set<PipelineFactory> pipelineFactories) {
    this.pipelineFactories = pipelineFactories;
  }
}
