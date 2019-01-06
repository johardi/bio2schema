# Schema.org Transformation for ClinicalTrials.gov Data

This folder contains a data transformation pipeline that will transform [ClinicalTrials.gov](https://clinicaltrials.gov/) data into Schema.org's structure.

The pipeline will transform the format from XML to JSON-LD following the [MedicalTrial](https://health-lifesci.schema.org/MedicalTrial) specification. Additionally, the pipeline includes some data reconciliations that will verify the value strings against a known knowledge base (e.g., data dictionary, public registry, ontology, etc.) and retrieve the identifier and standard naming. The goal for this extra step is to make sure that the resulting data is linkable to other sources for a richer data querying.

## Data Mapping

The table below shows the data mapping used to transform ClinicalTrials.gov XML data to Schema.org specification.
* The `Schema.org Data Element` column shows the data field name in the Schema.org specification
* The `ClinicalTrials.gov Data Element` column shows the XML path to denote the source value
* The `Type` column indicates the data type used to encapsulate the value
* The `Cardinality` column indicates the data structure used to store the value (i.e., 1 = a single value, * = an array, 1..* = either a single value or an array).
* Lastly, the `Note` column describes some additional information regarding the data transformation

| Schema.org Data Element | ClinicalTrials.gov Data Element | Type | Cardinality | Notes
--- | --- | :---: | :---: | ---
`identifier` | `/id_info/nct_id`,<br/>`/id_info/nct_alias`,<br/>`/id_info/org_study_id`,<br/>`/id_info/secondary_id` | [PropertyValue](https://schema.org/PropertyValue) | * |
`name` | `/official_title` | String | 1 | `/brief_title` (Alternative)
`description` | `/detailed_description/textblock` | String | 1 | `/brief_summary/textblock` (Alternative)
`status` | `/overall_status` | String | 1 |
`phase` | `/phase` | String | 1 |
`trialDesign` | `/study_design_info/intervention_model` | String | 1 |
`population` | `/eligibility/criteria/textblock` | String | 1..* | The pipeline will try to separate the inclusion and exclusion criteria
`url` | `/required_header/url` | URL | 1 |
`studySubject` | `/condition`,<br/>`/intervention` | [MedicalEntity](https://health-lifesci.schema.org/MedicalEntity) | * | Entity reconciliation was used to get the medical code via [BioPortal service](http://data.bioontology.org/documentation#nav_search)
`sponsor` | `/sponsors/lead_sponsor`,<br/>`/sponsors/collaborator` | [Organization](https://schema.org/Organization) | * | Entity reconciliation was used to get the entity id via [Dbpedia service](https://wiki.dbpedia.org/lookup)
`studyLocation` | `/facility/address/city` | [City](https://schema.org/City) | * | Entity reconciliation was used to get the entity id via [Dbpedia service](https://wiki.dbpedia.org/lookup)
`subjectOf` | `/reference`,<br/>`/results_reference` | [MedicalScholarlyArticle](https://health-lifesci.schema.org/MedicalScholarlyArticle) | * | Linkable to PubMed data set

We implemented the data mapping using the [JSLT](https://github.com/schibsted/jslt) syntax and you can find the detail in _./src/main/resources/clinicaltrials.jslt_ file.

## Example Output

The example below shows the result of executing the pipeline against a study titled ["Clinical Trial of Gabapentin to Improve Postoperative Pain in Surgical Patients"](https://clinicaltrials.gov/ct2/show/NCT00221338) in the ClinicalTrials.gov site.
```
{
  "@context": "http://schema.org",
  "@type": "MedicalTrial",
  "@id": "http://identifiers.org/clinicaltrials/NCT00221338",
  "identifier": [
    {
      "@type": "PropertyValue",
      "name": "Other",
      "value": "H5636-26795-01"
    },
    {
      "@type": "PropertyValue",
      "name": "NCT",
      "value": "NCT00221338"
    }
  ],
  "name": "Clinical Trial of Gabapentin to Improve Postoperative Pain in Surgical Patients",
  "description": "Postoperative delirium is a common condition, occurring in 10-70% of surgical patients after major surgery. [...]",
  "status": "Completed",
  "phase": "Phase 3",
  "trialDesign": "Single Group Assignment",
  "population": [
    "Inclusion Criteria:\n\n-  Male or female ≥45 years of age undergoing surgery involving the spine, hip or knee replacement. [...]",
    "Exclusion Criteria:\n\n-  Patients who take gabapentin preoperatively, or have known sensitivity to the drug, or those [...]."
  ],
  "url": "https://clinicaltrials.gov/show/NCT00221338",
  "studySubject": [
    {
      "@type": "MedicalCondition",
      "name": "Pain, Postoperative",
      "additionalType": "Condition or disease",
      "alternateName": [
        "Postoperative Pain",
        "Postoperative Pains"
      ],
      "code": {
        "@type": "MedicalCode",
        "@id": "http://identifiers.org/mesh/D010149",
        "codeValue": "D010149",
        "codingSystem": "MESH"
      }
    },
    {
      "@type": "Drug",
      "name": "gabapentin",
      "additionalType": "Intervention or treatment",
      "alternateName": [
        "Convalis",
        "Novo-Gabapentin",
        "Gabapentin-ratiopharm",
        "Apo-Gabapentin",
        "Gabapentin Stada",
        "Gabapentin Hexal",
        "Neurontin",
        "1-(aminomethyl)cyclohexaneacetic acid",
        "PMS-Gabapentin"
      ],
      "code": {
        "@type": "MedicalCode",
        "@id": "http://identifiers.org/mesh/C040029",
        "codeValue": "C040029",
        "codingSystem": "MESH"
      }
    }
  ],
  "sponsor": [
    {
      "@type": "Organization",
      "name": "University of California, San Francisco",
      "additionalType": "Lead Sponsor",
      "@id": "http://dbpedia.org/resource/University_of_California,_San_Francisco"
    }
  ],
  "studyLocation": [
    {
      "@type": "City",
      "name": "San Francisco",
      "@id": "http://dbpedia.org/resource/San_Francisco"
    }
  ],
  "subjectOf": [
    {
      "@type": "MedicalScholarlyArticle",
      "@id": "http://identifiers.org/pubmed/16914695",
      "name": "Pilot clinical trial of gabapentin to decrease postoperative delirium in older patients"
    }
  ]
}
```
_(Note: Some lengthy paragraphs in the example are truncated for a presentation purpose)_

## Related Resources

Here you can find several related resources used to construct the data mapping:
* https://health-lifesci.schema.org/MedicalTrial: The Schema.org specification for MedicalTrial class.
* https://clinicaltrials.gov/AllPublicXML.zip: The complete downloadable ClinicalTrials.gov data set in XML format
* https://clinicaltrials.gov/ct2/html/images/info/public.xsd: XML Schema for ClinicalTrials.gov public XML data set

The use of ClinicalTrials.gov data is subject to these [Terms and Conditions](https://clinicaltrials.gov/ct2/about-site/terms-conditions).
