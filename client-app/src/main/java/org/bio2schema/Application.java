package org.bio2schema;

import java.io.File;
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
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.base.Stopwatch;

public class Application {

  private static Logger logger = LogManager.getRootLogger();

  private static ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) throws IOException {

    logger.info("--- setting-up-pipline ---");
    final Stopwatch stopwatch = Stopwatch.createStarted();
    String datasetName = args[0];
    PipelineManager pipelineManager = Platform.getPipelineManager();
    logger.info("Initializing a pipeline for '{}' dataset", datasetName);
    logger.info("");
    Optional<Pipeline> foundPipeline = pipelineManager.getPipelineFor(datasetName);
    if (foundPipeline.isPresent()) {
      logger.info("--- source-data-location ---");
      String dataArgument = args[1];
      Path dataSourcePath = Paths.get(dataArgument);
      logger.info("Using " + dataSourcePath);
      logger.info("");
      int numberOfThreads = (args.length == 3) ? Integer.parseInt(args[2]) : 1;
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
      if (isDirectory(dataSourcePath)) {
        processInputDirectory(pipeline, dataSourcePath, numberOfThreads);
      } else {
        processInputFile(pipeline, dataSourcePath);
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

  private static void processInputDirectory(Pipeline pipeline, Path dirPath, int numberOfThreads) {
    try {
      ForkJoinPool fjp = new ForkJoinPool(numberOfThreads);
      List<CompletableFuture<Boolean>> list =
          Files.walk(dirPath).filter(filePath -> filePath.toString().endsWith(".xml"))
              .map(filePath -> CompletableFuture
                  .supplyAsync(() -> processInputFile(pipeline, filePath), fjp))
              .collect(Collectors.toList());
      list.stream().map(CompletableFuture::join).forEach(item -> {
        /* Does nothing */ });
    } catch (IOException e) {
      logger.error("Error while scanning the directory");
      logger.error(e);
    }
  }

  private static boolean processInputFile(Pipeline pipeline, Path filePath) {
    boolean success = true;
    try {
      logger.info("Processing {}", filePath.getFileName());
      Reader reader = new FileReader(filePath.toFile(), StandardCharsets.UTF_8);
      JsonNode input = JacksonUtils.readXmlAsJson(reader);
      JsonNode output = pipeline.process(input);
      writeOutput(filePath, output);
    } catch (Exception e) {
      logger.error(e.getMessage());
      success = false;
    }
    return success;
  }

  private static void writeOutput(Path filePath, JsonNode content) throws IOException {
    String parent = filePath.getParent().toString();
    String filename = getFileName(filePath) + ".json";
    ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
    writer.writeValue(new File(parent, filename), content);
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
