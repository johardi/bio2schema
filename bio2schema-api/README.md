# Bio2Schema API Libraries

## Pipeline API

A Bio2Schema pipeline consists of:
* One data transformation implementation, and
* Zero or many data processing implementations

Illustration:
```
Input --> [Data Transformation] --> [Data Processor] --> ... --> [Data Processor] --> Output
```

The `PipelineFactory` interface is used to construct the pipeline instance. A code snippet below shows an example of constructing a pipeline for ClinicalTrials.gov data _(note: some details were hidden for brevity)_.

```
public final class ClinicalTrialsPipelineFactory implements PipelineFactory {

  @Override
  public Pipeline createPipeline() {
    final EntityReconciler<GenericEntity> dbpediaLookupForCity =  ...
    final EntityReconciler<GenericEntity> dbpediaLookupForOrganization = ...
    final EntityReconciler<MedicalEntity> bioPortalSearch = ...
    final EntityReconciler<MedicalEntity> drugBankLookup = ...
    return new Pipeline(
        new MedicalTrialTransformer(),
        new StudyPopulationProcessor(),
        new StudyLocationProcessor(dbpediaLookupForCity),
        new StudySponsorProcessor(dbpediaLookupForOrganization),
        new StudySubjectProcessor()
          .addSubProcessor(new ReconcileMedicalTerm(bioPortalSearch))
          .addSubProcessor(new ReconcileDrugTerm(drugBankLookup)));
  }
  
  @Override
  public boolean isFactoryFor(String datasetName) {
    return "ClinicalTrials".equals(datasetName);
  }
}
```

## Reconciliation API

The `EntityReconciler` interface defines the basic method for implementing an entity reconciler service. The main function of the method is to find the preferred form of a given string name so that it can be linked to other known resources. Sources used for such reconciliation include name registries, data thesauri, linked-data databases, and ontologies. Selection of the source depends on the trustworthiness of the organization responsible, subject matter and richness of the information.

The API provides two abstract classes:

* `StandardEntityReconciler`: A generic code template for implementing an entity reconciler.
* `CachedEntityReconciler`: An cache-based entity reconciler to improve searching performance; recommended when the implementation makes use of a remote REST service.


## Recognition API

The `EntityRecognition` interface defines the basic method for implementing a named entity recognition (NER) service. You might want to implement the interface as a wrapper class for any existing NER libraries in the market.