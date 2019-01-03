package bio2schema.reconciler.drugbank;

import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bio2schema.api.reconciliation.CachedEntityReconciler;
import org.bio2schema.api.reconciliation.Database;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.util.JacksonUtils;

public class DrugBankLookup extends CachedEntityReconciler<MedicalEntity> {

  public DrugBankLookup(@Nonnull Database database) {
    super("DrugBank CSV Lookup", database);
  }

  @Nullable
  @Override
  public DrugEntry handleResult(String inputString, Object outputResult) throws IOException {
    if (outputResult == null) {
      return null;
    }
    return JacksonUtils.mapper.convertValue(outputResult, DrugEntry.class);
  }
}
