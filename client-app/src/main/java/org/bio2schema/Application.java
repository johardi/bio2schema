package org.bio2schema;

import java.io.File;
import java.io.FileOutputStream;
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
import org.bio2schema.PipelineExecutor.ResultBundle;
import org.bio2schema.api.pipeline.Pipeline;
import org.bio2schema.apibinding.PipelineManager;
import org.bio2schema.apibinding.Platform;
import org.bio2schema.util.JacksonUtils;
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
      File outputDirectory = outputLocation.toFile();
      if (!outputDirectory.exists()) {
        outputDirectory.mkdir();
      }
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
        processInputDirectory(executor, inputLocation, outputLocation, numberOfThreads);
      } else {
        processInputFile(executor, inputLocation, outputLocation);
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

  private static void processInputDirectory(PipelineExecutor executor, Path inputDirectory,
      Path outputDirectory, int numberOfThreads) {
    try {
      ForkJoinPool fjp = new ForkJoinPool(numberOfThreads);
      Files.walk(inputDirectory)
          .filter(inputLocation -> inputLocation.toString().endsWith(".xml"))
          .map(inputLocation -> CompletableFuture
              .supplyAsync(() -> executor.submit(inputLocation), fjp))
          .collect(Collectors.toList())
          .stream()
          .map(CompletableFuture::join)
          .forEach(result -> {
            writeResultBundle(result, outputDirectory);
          });
    } catch (IOException e) {
      logger.error("Error while processing input directory [{}]", inputDirectory);
      logger.error(e);
    }
  }

  private static void processInputFile(PipelineExecutor executor, Path inputLocation, Path outputDirectory) {
    ResultBundle result = executor.submit(inputLocation);
    writeResultBundle(result, outputDirectory);
  }

  private static void writeResultBundle(ResultBundle result, Path outputDirectory) {
    try {
      JsonNode content = result.getContent();
      Path outputLocation = createOutputLocation(outputDirectory, result.getSourceInput());
      JacksonUtils.prettyPrint(content, new FileOutputStream(outputLocation.toFile()));
      logger.info("Succeed transforming document: [{}]", result.getSourceInput().getFileName());
    } catch (Exception e) {
      logger.error("Failed transforming document: [{}]", result.getSourceInput().getFileName());
      logger.error(e.getMessage());
    }
  }

  private static Path createOutputLocation(Path outputDirectory, Path inputLocation) {
    String fileName = inputLocation.getFileName().toString();
    int i = fileName.lastIndexOf('.');
    if (i > 0 && i < fileName.length() - 1) {
      String extension = fileName.substring(i);
      fileName = fileName.replace(extension, "");
    }
    return Paths.get(outputDirectory.toString(), fileName + ".json");
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
