package org.bio2schema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bio2schema.api.pipeline.Pipeline;
import org.bio2schema.apibinding.PipelineManager;
import org.bio2schema.apibinding.Platform;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Stopwatch;

public class Application {

  private static Logger logger = LogManager.getRootLogger();

  public static void main(String[] args) throws IOException {

    logger.info("--- setting-up-pipline ---");
    final Stopwatch stopwatch = Stopwatch.createStarted();
    String datasetName = args[0];
    PipelineManager pipelineManager = Platform.getPipelineManager();
    logger.info("Initializing a pipeline for '{}' dataset", datasetName);
    logger.info("");
    Optional<Pipeline> foundPipeline = pipelineManager.getPipelineFor(datasetName);
    if (foundPipeline.isPresent()) {
      logger.info("--- input-data-location ---");
      Path inputLocation = Paths.get(args[1]);
      logger.info("Using " + inputLocation);
      logger.info("");
      logger.info("--- output-result-location ---");
      Path outputLocation = Paths.get(args[2]);
      DocumentWriter writer = new DocumentWriter(outputLocation);
      logger.info("Using " + outputLocation);
      logger.info("");
      int numberOfThreads = (args.length == 4) ? Integer.parseInt(args[3]) : 1;
      logger.info("--- number-of-threads ---");
      if (numberOfThreads <= 0) {
        logger.warn("Invalid number for number-of-threads argument: {}", numberOfThreads);
        logger.warn("Using the default single thread processing");
        numberOfThreads = 1;
      } else if (numberOfThreads == 1) {
        logger.info("Using a single thread processing");
      } else {
        logger.info("Using parallel processing of {} threads", numberOfThreads);
      }
      logger.info("");
      logger.info("--- pipeline-exec ---");
      final Pipeline pipeline = foundPipeline.get();
      PipelineExecutor executor = new PipelineExecutor(pipeline);
      if (isDirectory(inputLocation)) {
        processInputDirectory(inputLocation, executor, writer, numberOfThreads);
      } else {
        processInputFile(inputLocation, executor, writer);
      }
      logger.info("");
      logger.info("TASK DONE in {}", formatDuration(stopwatch));
    } else {
      logger.error("TASK FAILED in {}", formatDuration(stopwatch));
    }
  }

  private static boolean isDirectory(Path location) {
    return Files.isDirectory(location);
  }

  private static void processInputDirectory(Path dirPath, PipelineExecutor executor,
      DocumentWriter writer, int numberOfThreads) {
    try {
      ForkJoinPool fjp = new ForkJoinPool(numberOfThreads);
      Files.walk(dirPath)
          .filter(filePath -> filePath.toString().endsWith(".xml"))
          .map(filePath -> CompletableFuture
              .supplyAsync(() -> executor.submit(filePath.toFile()), fjp))
          .collect(Collectors.toList())
          .parallelStream()
          .map(CompletableFuture::join)
          .forEach(result -> writer.writeToFile(result));
    } catch (IOException e) {
      logger.error("Error while processing input directory [{}]", dirPath);
      logger.error(e);
    }
  }

  private static void processInputFile(Path filePath, PipelineExecutor executor,
      DocumentWriter writer) {
    ResultObject result = executor.submit(filePath.toFile());
    writer.writeToFile(result);
  }

  private static String formatDuration(Stopwatch stopwatch) {
    long duration = stopwatch.elapsed(TimeUnit.SECONDS);
    long seconds = duration % 60;
    long minutes = (duration / 60) % 60;
    long hours = (duration / (60 * 60)) % 24;
    long days = (duration / (60 * 60 * 24));
    if (duration < 60) {
      return String.format("%ds", seconds);
    } else if (duration < 3600) {
      return String.format("%dm %ds", minutes, seconds);
    } else if (duration < 86400) {
      return String.format("%dh %dm %ds", hours, minutes, seconds);
    } else {
      return String.format("%dd %dh %dm %ds", days, hours, minutes, seconds);
    }
  }
}
