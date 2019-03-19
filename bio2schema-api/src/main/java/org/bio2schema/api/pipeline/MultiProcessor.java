package org.bio2schema.api.pipeline;

public interface MultiProcessor extends Processor {

  void registerProcessor(Processor processor);
}
