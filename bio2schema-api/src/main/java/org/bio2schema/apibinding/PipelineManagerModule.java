package org.bio2schema.apibinding;

import java.util.Collection;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bio2schema.api.pipeline.PipelineFactory;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class PipelineManagerModule extends AbstractModule {

  private static Logger logger = LogManager.getRootLogger();

  @Override
  public void configure() {
      Multibinder<PipelineFactory> binder = Multibinder.newSetBinder(binder(), PipelineFactory.class);
      load(PipelineFactory.class).forEach(o -> binder.addBinding().toInstance(o));
  }

  private <T> Iterable<T> load(Class<T> type) {
      Collection<T> result = Lists.newArrayList();
      try {
          ServiceLoader.load(type).forEach(result::add);
          if (result.isEmpty()) {
              ClassLoader classLoader = getClass().getClassLoader();
              ServiceLoader.load(type, classLoader).forEach(result::add);
          }
      } catch (ServiceConfigurationError e) {
          logger.warn(String.format(
              "A problem occurred loading module '%s'\n > %s",
              type, e.getMessage()));
      }
      return result;
  }
}
