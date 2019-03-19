package org.bio2schema.pipeline.drugbank;

import org.bio2schema.api.pipeline.Pipeline;
import org.bio2schema.api.pipeline.PipelineFactory;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.pipeline.drugbank.DrugNameProcessor.ReconcileDrugRxCui;
import org.bio2schema.pipeline.drugbank.DrugNameProcessor.ReconcileDrugTerm;
import org.bio2schema.reconcilers.bioportal.BioPortalSearch;
import org.bio2schema.reconcilers.bioportal.BioPortalService;

public final class DrugBankPipelineFactory implements PipelineFactory {

  @Override
  public Pipeline createPipeline() {
    final EntityReconciler<MedicalEntity> meshSearch = setupMeshBioPortalSearch();
    final EntityReconciler<MedicalEntity> rxNormSearch = setupRxNormBioPortalSearch();
    return new Pipeline(
        new DrugBankTransformer(),
        new DrugNameProcessor()
          .addSubProcessor(new ReconcileDrugTerm(meshSearch))
          .addSubProcessor(new ReconcileDrugRxCui(rxNormSearch)));
  }

  private EntityReconciler<MedicalEntity> setupMeshBioPortalSearch() {
    return new BioPortalSearch(new BioPortalService());
  }

  private EntityReconciler<MedicalEntity> setupRxNormBioPortalSearch() {
    BioPortalService service = new BioPortalService();
    service.setTargetOntologies("RXNORM");
    return new BioPortalSearch(service);
  }

  @Override
  public boolean isFactoryFor(String datasetName) {
    return "DrugBank".equals(datasetName);
  }
}
