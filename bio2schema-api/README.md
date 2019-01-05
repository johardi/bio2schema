# Bio2Schema API Libraries

## Pipeline API

A Bio2Schema pipeline consists of:
* One data transformation implementation
* Zero or many data processing implementations

Illustration:
```
Input --> [Data Transformation] --> [Data Processor] --> ... --> [Data Processor] --> Output
```

An implementation of `PipelineFactory` is required to customize the data transformation pipeline that you want to construct. A code snippet below shows an example of constructing a pipeline for ClinicalTrials.gov data _(note: some details were hidden for brevity)_.

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

The implementation for an entity reconciler is based on the `EntityReconciler` interface and it can be useful as a module for the data processor. The API provides two abstract classes:

* `StandardEntityReconciler`: A generic skeleton for implementing an entity reconciliation module.
* `CachedEntityReconciler`: An cache-based module implementation for resource efficiency; recommended when the module relies on a remote REST call.


## Recognition API

The implementation for an entity recongnition is based on the `EntityRecognition` interface and it can be useful as a module for the data processor.