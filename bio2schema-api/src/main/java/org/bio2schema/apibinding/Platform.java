package org.bio2schema.apibinding;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Platform {

  public static PipelineManager getPipelineManager() {
    Injector injector = Guice.createInjector(new PipelineManagerModule());
    PipelineManager pipelineManager = injector.getInstance(PipelineManager.class);
    return pipelineManager;
  }
}
