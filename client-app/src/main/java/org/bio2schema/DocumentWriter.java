package org.bio2schema;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bio2schema.util.JacksonUtils;

public class DocumentWriter {

  private static Logger logger = LogManager.getRootLogger();

  private final Path outputLocation;

  public DocumentWriter(@Nonnull Path outputLocation) {
    this.outputLocation = checkNotNull(outputLocation);
    createDirectoryWhenNotPresent(outputLocation);
  }

  private static void createDirectoryWhenNotPresent(Path outputLocation) {
    File directory = outputLocation.toFile();
    if (!directory.exists()) {
      directory.mkdir();
    }
  }

  public void writeToFile(ResultObject result) {
    String fileName = useSameNameAsInput(result.getInputLocation());
    writeToFile(result, fileName);
  }

  public void writeToFile(ResultObject result, String fileNameWithoutExtension) {
    try {
      String fileName = fileNameWithoutExtension + ".json";
      Path fileLocation = Paths.get(outputLocation.toString(), fileName);
      JacksonUtils.prettyPrint(result.getContent(), new FileOutputStream(fileLocation.toFile()));
      logger.info("Succeed transforming document: [{}]", result.getInputLocation().getFileName());
    } catch (Exception e) {
      logger.error("Failed transforming document: [{}]", result.getInputLocation().getFileName());
      logger.error(e.getMessage());
    }
  }

  private static String useSameNameAsInput(Path inputLocation) {
    String fileName = inputLocation.getFileName().toString();
    return removeFileExtension(fileName);
  }

  private static String removeFileExtension(String fileName) {
    int i = fileName.lastIndexOf('.');
    if (i > 0 && i < fileName.length() - 1) {
      String extension = fileName.substring(i);
      fileName = fileName.replace(extension, "");
    }
    return fileName;
  }
}
