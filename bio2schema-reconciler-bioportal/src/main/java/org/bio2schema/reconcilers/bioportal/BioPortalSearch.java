package org.bio2schema.reconcilers.bioportal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bio2schema.api.reconciliation.Database;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BioPortalSearch implements EntityReconciler<MedicalEntity> {

  private final Database database;

  public BioPortalSearch(@Nonnull Database database) {
    this.database = checkNotNull(database);
  }

  @Override
  public Optional<MedicalEntity> reconcile(String inputString) throws IOException {
    try {
      Object outputResult = database.find(inputString);
      BioPortalArticle article = handleResult(inputString, outputResult);
      return Optional.ofNullable(article);
    } catch (IOException e) {
      String message = String.format(
          "A problem occurred using reconciler '%s'\n> %s",
          database.getName(), e.getMessage());
      throw new IOException(message);
    }
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
