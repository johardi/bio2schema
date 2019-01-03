package org.bio2schema.reconcilers.bioportal;

import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bio2schema.api.reconciliation.CachedEntityReconciler;
import org.bio2schema.api.reconciliation.Database;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BioPortalSearch extends CachedEntityReconciler<MedicalEntity> {

  public BioPortalSearch(@Nonnull Database database) {
    super("BioPortal Search", database);
  }

  @Nullable
  @Override
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
