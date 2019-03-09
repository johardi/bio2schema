package org.bio2schema.reconcilers.bioportal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static java.lang.String.format;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bio2schema.api.reconciliation.Database;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.HttpRequestException;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BioPortalSearch implements EntityReconciler<MedicalEntity> {

  private static final int MAX_RETRY = 3;

  private final Database database;

  public BioPortalSearch(@Nonnull Database database) {
    this.database = checkNotNull(database);
  }

  @Override
  public Optional<MedicalEntity> reconcile(String inputString) throws IOException {
    String detailError = null;
    for (int i = 0; i < MAX_RETRY; i++) {
      try {
        Object outputResult = database.find(inputString);
        BioPortalArticle article = handleResult(inputString, outputResult);
        return Optional.ofNullable(article);
      } catch (IOException e) {
        detailError = e.getMessage();
        if (e instanceof HttpRequestException) {
          HttpRequestException rex = (HttpRequestException) e;
          if (rex.getResponseCode() == 408 || rex.getResponseCode() == 429) {
            try {
              sleepForSomeSeconds(i+1);
              continue;
            } catch (InterruptedException ie) {
              detailError = ie.getMessage();
              break;
            }
          }
        }
        break;
      }
    }
    throw new IOException(format("A problem occurred using reconciler '%s'\n> %s",
        database.getName(), detailError));
  }

  private static void sleepForSomeSeconds(int second) throws InterruptedException {
    TimeUnit.SECONDS.sleep(second);
  }

  @Nullable
  public BioPortalArticle handleResult(String inputString, Object outputResult) throws IOException {
    ObjectNode objectNode = checkIfObjectNode(outputResult);
    BioPortalArticle[] results =
        JacksonUtils.mapper.treeToValue(objectNode.get("collection"), BioPortalArticle[].class);
    return filterTopResult(results);
  }

  private BioPortalArticle filterTopResult(BioPortalArticle[] results) {
    if (results.length == 0) {
      return null;
    }
    return results[0];
  }
}
