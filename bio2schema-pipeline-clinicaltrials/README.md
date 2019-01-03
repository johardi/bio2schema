# An ETL Pipeline for ClinicalTrials.gov

This folder contains the implementation of a data pipeline that will transform [ClinicalTrials.gov](https://clinicaltrials.gov/) XML data dump to a Schema.org format under the [MedicalTrial](https://health-lifesci.schema.org/MedicalTrial) concept type.

The data mapping written in JSLT language can be found [here](https://github.com/johardi/bio2schema/bio2schema-pipeline-clinicaltrials/blob/master/src/main/resources/clinicaltrials.jslt)

Some highlights:
* Separate the inclusion and exclusion criteria in the study population.
* Reconcile study locations and study sponsors using the [DBpedia](https://dbpedia.org/) KB database.
* Reconcile study subjects using the [BioPortal](https://bioportal.bioontology.org/) terms database. This feature requires BIOPORTAL_APIKEY in the system environment.