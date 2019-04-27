package org.bio2schema.pipeline.geo;

import static org.bio2schema.vocab.SchemaOrg.TYPE_COLLEGE_OR_UNIVERSITY;
import static org.bio2schema.vocab.SchemaOrg.TYPE_EDUCATIONAL_ORGANIZATION;
import static org.bio2schema.vocab.SchemaOrg.TYPE_GOVERNMENT_ORGANIZATION;
import static org.bio2schema.vocab.SchemaOrg.TYPE_HOSPITAL;
import static org.bio2schema.vocab.SchemaOrg.TYPE_ORGANIZATION;
import org.bio2schema.api.pipeline.Pipeline;
import org.bio2schema.api.pipeline.PipelineFactory;
import org.bio2schema.api.reconciliation.CachedEntityReconciler;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import org.bio2schema.api.reconciliation.entitytype.MedicalEntity;
import org.bio2schema.reconcilers.bioportal.BioPortalSearch;
import org.bio2schema.reconcilers.bioportal.BioPortalService;
import org.bio2schema.reconcilers.dbpedia.DbpediaLookup;
import org.bio2schema.reconcilers.dbpedia.DbpediaService;

public class GeoPipelineFactory implements PipelineFactory {

  @Override
  public Pipeline createPipeline() {
    final EntityReconciler<GenericEntity> dbpediaLookupForOrganization = setupDbpediaLookupForOrganization();
    final EntityReconciler<MedicalEntity> bioPortalSearch = setupBioPortalSearch();
    return new Pipeline(
        new DataCatalogTransformer(),
        new CreatorAffiliationProcessor(dbpediaLookupForOrganization),
        new SampleOrganismProcessor(bioPortalSearch));
  }

  private EntityReconciler<GenericEntity> setupDbpediaLookupForOrganization() {
    DbpediaService dbpediaService = new DbpediaService();
    DbpediaLookup dbpediaLookup = new DbpediaLookup(dbpediaService);
    dbpediaLookup.searchOnly(TYPE_ORGANIZATION,
        TYPE_COLLEGE_OR_UNIVERSITY,
        TYPE_EDUCATIONAL_ORGANIZATION,
        TYPE_GOVERNMENT_ORGANIZATION,
        TYPE_HOSPITAL);
    return new CachedEntityReconciler<>(dbpediaLookup, 100);
  }

  private EntityReconciler<MedicalEntity> setupBioPortalSearch() {
    BioPortalSearch bioPortalSearch = new BioPortalSearch(new BioPortalService());
    return new CachedEntityReconciler<>(bioPortalSearch, 100);
  }

  @Override
  public boolean isFactoryFor(String datasetName) {
    return "GEO".equals(datasetName);
  }
}
