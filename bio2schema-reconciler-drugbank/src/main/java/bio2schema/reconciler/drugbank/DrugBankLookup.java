package bio2schema.reconciler.drugbank;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bio2schema.api.reconciliation.Database;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.util.JacksonUtils;

public class DrugBankLookup implements EntityReconciler<MedicalEntity> {

  private final Database database;

  public DrugBankLookup(@Nonnull Database database) {
    this.database = checkNotNull(database);
  }

  @Override
  public Optional<MedicalEntity> reconcile(String inputString) throws IOException {
    try {
      Object outputResult = database.find(inputString);
      DrugEntry entry = handleResult(inputString, outputResult);
      return Optional.ofNullable(entry);
    } catch (IOException e) {
      String message = String.format(
          "A problem occurred using reconciler '%s'\n> %s",
          database.getName(), e.getMessage());
      throw new IOException(message);
    }
  }

  @Nullable
  public DrugEntry handleResult(String inputString, Object outputResult) throws IOException {
    if (outputResult == null) {
      return null;
    }
    return JacksonUtils.mapper.convertValue(outputResult, DrugEntry.class);
  }
}
