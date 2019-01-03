package org.bio2schema.pipeline.drugbank;

import org.bio2schema.api.pipeline.Pipeline;
import org.bio2schema.api.pipeline.PipelineFactory;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.reconcilers.bioportal.BioPortalSearch;
import org.bio2schema.reconcilers.bioportal.BioPortalService;

public final class DrugBankPipelineFactory implements PipelineFactory {

  @Override
  public Pipeline createPipeline() {
    final EntityReconciler<MedicalEntity> bioPortalSearch = setupBioPortalSearch();
    return new Pipeline(
        new DrugBankTransformer(),
        new DrugNameProcessor(bioPortalSearch));
  }

  private BioPortalSearch setupBioPortalSearch() {
    return new BioPortalSearch(new BioPortalService());
  }

  @Override
  public boolean isFactoryFor(String datasetName) {
    return "DrugBank".equals(datasetName);
  }
}
