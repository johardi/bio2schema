package org.bio2schema;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
      String inputLocation = args[1];
      Path inputLocationPath = Paths.get(inputLocation);
      logger.info("Using " + inputLocationPath);
      logger.info("");
      logger.info("--- output-result-location ---");
      String outputLocation = args[2];
      Path outputLocationPath = Paths.get(outputLocation);
      logger.info("Using " + outputLocationPath);
      logger.info("");
      int numberOfThreads = (args.length == 4) ? Integer.parseInt(args[3]) : 1;
      logger.info("--- number-of-threads ---");
      if (numberOfThreads <= 0) {
        logger.warn("Invalid number for number-of-threads argument: {}", numberOfThreads);
        logger.warn("Using the default single thread processing");
        numberOfThreads = 1;
      }
      if (numberOfThreads == 1) {
        logger.info("Using a single thread processing");
      } else {
        logger.info("Using parallel processing of {} threads", numberOfThreads);
      }
      logger.info("");
      logger.info("--- pipeline-exec ---");
      final Pipeline pipeline = foundPipeline.get();
      if (isDirectory(inputLocationPath)) {
        processInputDirectory(pipeline, inputLocationPath, outputLocationPath, numberOfThreads);
      } else {
        processInputFile(pipeline, inputLocationPath, outputLocationPath);
      }
      logger.info("");
      logger.info("TASK DONE in {}", formatDuration(stopwatch));
    } else {
      logger.error("TASK FAILED in {}", formatDuration(stopwatch));
    }
  }

  private static boolean isDirectory(Path path) {
    return Files.isDirectory(path);
  }

  private static void processInputDirectory(Pipeline pipeline, Path inputDirPath,
      Path outputDirPath, int numberOfThreads) {
    try {
      ForkJoinPool fjp = new ForkJoinPool(numberOfThreads);
      List<CompletableFuture<Boolean>> list =
          Files.walk(inputDirPath).filter(filePath -> filePath.toString().endsWith(".xml"))
              .map(filePath -> CompletableFuture
                  .supplyAsync(() -> processInputFile(pipeline, filePath, outputDirPath), fjp))
              .collect(Collectors.toList());
      list.stream().map(CompletableFuture::join).forEach(item -> {
        /* Does nothing */ });
    } catch (IOException e) {
      logger.error("Error while scanning the directory");
      logger.error(e);
    }
  }

  private static boolean processInputFile(Pipeline pipeline, Path inputFilePath,
      Path outputDirPath) {
    boolean success = true;
    try {
      logger.info("Processing {}", inputFilePath.getFileName());
      Reader reader = new FileReader(inputFilePath.toFile(), StandardCharsets.UTF_8);
      JsonNode input = JacksonUtils.readXmlAsJson(reader);
      JsonNode output = pipeline.process(input);
      String outputFileName = getFileName(inputFilePath) + ".json";
      Path outputFilePath = Paths.get(outputDirPath.toString(), outputFileName);
      writeOutput(outputFilePath, output);
    } catch (Exception e) {
      logger.error(e.getMessage());
      success = false;
    }
    return success;
  }

  private static void writeOutput(Path outputFilePath, JsonNode content) throws IOException {
    JacksonUtils.prettyPrint(content, new FileOutputStream(outputFilePath.toFile()));
  }

  private static String getFileName(Path inputPath) {
    String fileName = inputPath.getFileName().toString();
    int i = fileName.lastIndexOf('.');
    if (i > 0 && i < fileName.length() - 1) {
      String extension = fileName.substring(i);
      fileName = fileName.replace(extension, "");
    }
    return fileName;
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
