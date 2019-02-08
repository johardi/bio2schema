package org.bio2schema.pipeline.clinicaltrials;

import static org.bio2schema.vocab.SchemaOrg.TYPE_CITY;
import static org.bio2schema.vocab.SchemaOrg.TYPE_COLLEGE_OR_UNIVERSITY;
import static org.bio2schema.vocab.SchemaOrg.TYPE_EDUCATIONAL_ORGANIZATION;
import static org.bio2schema.vocab.SchemaOrg.TYPE_GOVERNMENT_ORGANIZATION;
import static org.bio2schema.vocab.SchemaOrg.TYPE_HOSPITAL;
import static org.bio2schema.vocab.SchemaOrg.TYPE_ORGANIZATION;
import static org.bio2schema.vocab.SchemaOrg.TYPE_PLACE;
import org.bio2schema.api.pipeline.Pipeline;
import org.bio2schema.api.pipeline.PipelineFactory;
import org.bio2schema.api.reconciliation.CachedEntityReconciler;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.pipeline.clinicaltrials.StudySubjectProcessor.ReconcileDrugTerm;
import org.bio2schema.pipeline.clinicaltrials.StudySubjectProcessor.ReconcileMedicalTerm;
import org.bio2schema.reconcilers.bioportal.BioPortalSearch;
import org.bio2schema.reconcilers.bioportal.BioPortalService;
import org.bio2schema.reconcilers.dbpedia.DbpediaLookup;
import org.bio2schema.reconcilers.dbpedia.DbpediaService;
import bio2schema.reconciler.drugbank.DrugBankDatabase;
import bio2schema.reconciler.drugbank.DrugBankLookup;

public final class ClinicalTrialsPipelineFactory implements PipelineFactory {

  @Override
  public Pipeline createPipeline() {
    final EntityReconciler<GenericEntity> dbpediaLookupForCity = setupDbpediaLookupForCity();
    final EntityReconciler<GenericEntity> dbpediaLookupForOrganization = setupDbpediaLookupForOrganization();
    final EntityReconciler<MedicalEntity> bioPortalSearch = setupBioPortalSearch();
    final EntityReconciler<MedicalEntity> drugBankLookup = setupDrugBankLookup();
    return new Pipeline(
        new MedicalTrialTransformer(),
        new StudyPopulationProcessor(),
        new StudyLocationProcessor(dbpediaLookupForCity),
        new StudySponsorProcessor(dbpediaLookupForOrganization),
        new StudySubjectProcessor()
          .addSubProcessor(new ReconcileMedicalTerm(bioPortalSearch))
          .addSubProcessor(new ReconcileDrugTerm(drugBankLookup)));
  }

  private EntityReconciler<GenericEntity> setupDbpediaLookupForCity() {
    final DbpediaService dbpediaService = new DbpediaService();
    DbpediaLookup dbpediaLookup = new DbpediaLookup(dbpediaService);
    dbpediaLookup.searchOnly(TYPE_CITY, TYPE_PLACE);
    return new CachedEntityReconciler<>(dbpediaLookup, 500);
  }

  private EntityReconciler<GenericEntity> setupDbpediaLookupForOrganization() {
    final DbpediaService dbpediaService = new DbpediaService();
    DbpediaLookup dbpediaLookup = new DbpediaLookup(dbpediaService);
    dbpediaLookup.searchOnly(TYPE_ORGANIZATION,
        TYPE_COLLEGE_OR_UNIVERSITY,
        TYPE_EDUCATIONAL_ORGANIZATION,
        TYPE_GOVERNMENT_ORGANIZATION,
        TYPE_HOSPITAL);
    return new CachedEntityReconciler<>(dbpediaLookup, 500);
  }

  private EntityReconciler<MedicalEntity> setupBioPortalSearch() {
    BioPortalService bioPortalService = new BioPortalService();
    bioPortalService.setTargetOntologies("MESH,ICD10CM");
    BioPortalSearch bioPortalSeach = new BioPortalSearch(bioPortalService);
    return new CachedEntityReconciler<>(bioPortalSeach, 200);
  }

  private EntityReconciler<MedicalEntity> setupDrugBankLookup() {
    return new DrugBankLookup(new DrugBankDatabase());
  }

  @Override
  public boolean isFactoryFor(String datasetName) {
    return "ClinicalTrials".equals(datasetName);
  }
}
