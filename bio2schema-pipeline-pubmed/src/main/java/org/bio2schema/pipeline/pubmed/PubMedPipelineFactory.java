package org.bio2schema.pipeline.pubmed;

import static org.bio2schema.vocab.SchemaOrg.TYPE_COLLEGE_OR_UNIVERSITY;
import static org.bio2schema.vocab.SchemaOrg.TYPE_EDUCATIONAL_ORGANIZATION;
import static org.bio2schema.vocab.SchemaOrg.TYPE_GOVERNMENT_ORGANIZATION;
import static org.bio2schema.vocab.SchemaOrg.TYPE_HOSPITAL;
import static org.bio2schema.vocab.SchemaOrg.TYPE_ORGANIZATION;
import org.bio2schema.api.pipeline.Pipeline;
import org.bio2schema.api.pipeline.PipelineFactory;
import org.bio2schema.api.recognition.Classifier;
import org.bio2schema.api.recognition.EntityRecognizer;
import org.bio2schema.api.reconciliation.CachedEntityReconciler;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.pipeline.pubmed.ArticleAboutProcessor.ReconcileDrugTerm;
import org.bio2schema.pipeline.pubmed.ArticleAboutProcessor.ReconcileMedicalTerm;
import org.bio2schema.recognizers.stanfordner.English3ClassClassifier;
import org.bio2schema.recognizers.stanfordner.English3ClassClassifier.Type;
import org.bio2schema.recognizers.stanfordner.StanfordNer;
import org.bio2schema.reconcilers.bioportal.BioPortalSearch;
import org.bio2schema.reconcilers.bioportal.BioPortalService;
import org.bio2schema.reconcilers.dbpedia.DbpediaService;
import org.bio2schema.reconcilers.dbpedia.DbpediaSimilarityLookup;
import bio2schema.reconciler.drugbank.DrugBankDatabase;
import bio2schema.reconciler.drugbank.DrugBankLookup;

public final class PubMedPipelineFactory implements PipelineFactory {

  @Override
  public Pipeline createPipeline() {
    final EntityRecognizer stanfordNer = setupStanfordNer();
    final EntityReconciler<GenericEntity> dbpediaLookupForOrganization = setupDbpediaLookupForOrganization();
    final EntityReconciler<MedicalEntity> bioPortalSearch = setupBioPortalSearch();
    final EntityReconciler<MedicalEntity> drugBankLookup = setupDrugBankLookup();
    return new Pipeline(
        new MedicalScholarlyArticleTransformer(),
        new AuthorAffiliationProcessor(stanfordNer, dbpediaLookupForOrganization),
        new ArticleAboutProcessor()
          .addSubProcessor(new ReconcileMedicalTerm(bioPortalSearch))
          .addSubProcessor(new ReconcileDrugTerm(drugBankLookup)));
  }

  private StanfordNer setupStanfordNer() {
    final Classifier classifier = new English3ClassClassifier();
    return new StanfordNer(classifier, Type.ORGANIZATION.getString());
  }
  
  private EntityReconciler<GenericEntity> setupDbpediaLookupForOrganization() {
    DbpediaService dbpediaService = new DbpediaService();
    DbpediaSimilarityLookup dbpediaLookup = new DbpediaSimilarityLookup(dbpediaService, 0.965);
    dbpediaLookup.searchOnly(TYPE_ORGANIZATION,
        TYPE_COLLEGE_OR_UNIVERSITY,
        TYPE_EDUCATIONAL_ORGANIZATION,
        TYPE_GOVERNMENT_ORGANIZATION,
        TYPE_HOSPITAL);
    return new CachedEntityReconciler<>(dbpediaLookup, 500);
  }

  private EntityReconciler<MedicalEntity> setupBioPortalSearch() {
    BioPortalSearch bioPortalSearch = new BioPortalSearch(new BioPortalService());
    return new CachedEntityReconciler<>(bioPortalSearch, 250);
  }

  private EntityReconciler<MedicalEntity> setupDrugBankLookup() {
    return new DrugBankLookup(new DrugBankDatabase());
  }

  @Override
  public boolean isFactoryFor(String datasetName) {
    return "PubMed".equals(datasetName);
  }
}
