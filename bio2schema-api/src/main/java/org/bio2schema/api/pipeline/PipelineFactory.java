package org.bio2schema.api.pipeline;

public interface PipelineFactory {

  Pipeline createPipeline();

  boolean isFactoryFor(String datasetName);
}
