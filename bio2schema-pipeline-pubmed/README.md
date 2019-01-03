# An ETL Pipeline for PubMed

This folder contains the implementation of a data pipeline that will transform [PubMed](https://www.ncbi.nlm.nih.gov/pubmed/) XML data dump to a Schema.org format under the [MedicalScholarlyArticle](https://health-lifesci.schema.org/MedicalScholarlyArticle) concept type.

The data mapping written in JSLT language can be found [here](https://github.com/johardi/bio2schema/bio2schema-pipeline-pubmed/blob/master/src/main/resources/pubmed.jslt)

Some highlights:
* Reconcile author affiliations using the [DBpedia](https://dbpedia.org/) KB database.
* Reconcile article topics using the [BioPortal](https://bioportal.bioontology.org/) terms database (requires BIOPORTAL_APIKEY in the system environment).