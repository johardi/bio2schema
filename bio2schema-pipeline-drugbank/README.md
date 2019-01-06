# Schema.org Transformation for DrugBank Data

This folder contains a data transformation pipeline that will transform [DrugBank](https://www.drugbank.ca) data into Schema.org's structure.

The pipeline will transform the original data format from XML to JSON-LD following the [Drug](https://health-lifesci.schema.org/Drug) specification. Additionally, the pipeline will reconcile names with known LOD databases (e.g., DBpedia and BioPortal) to find their persistent identifier and preferred name so that it is possible to link the information with other well-known resources.

## Data Mapping

The table below shows the data mapping used to transform DrugBank XML data to Schema.org specification.
* The `Schema.org Data Element` column shows the data field name in the Schema.org specification
* The `DrugBank Data Element` column shows the XML path to denote the source value
* The `Type` column indicates the data type used to encapsulate the value
* The `Cardinality` column indicates the data structure used to store the value (i.e., 1 = a single value, * = an array, 1..* = either a single value or an array).
* Lastly, the `Note` column describes some additional information regarding the data transformation

| Schema.org Data Element | DrugBank Data Element | Type | Cardinality | Notes
--- | --- | :---: | :---: | ---
`identifier` | `/cas-number`,<br/>`/unii` | [PropertyValue](https://schema.org/PropertyValue) | * |
`name` | `/name` | String | 1 | An entity reconciler will be used to get the medical code via [BioPortal search service](http://data.bioontology.org/documentation#nav_search)
`description` | `/description` | String | 1 |
`alternateName` | `/synonyms/synonym` | String | * |
`drugClass` | `/classification/direct-parent` | String | 1 |
`activeIngredient` | `/salts/salt` | String | * |
`proprietaryName` | `/international_brands/international_brand` | String | * |
`clinicalPharmacology` | `/absorption`,<br/>`/metabolism`,<br/>`/route-of-elimination`,<br/>`/half-life`,<br/>`/clearance`,<br/>`/volume-of-distribution`,<br/>`/affected-organism`,<br/>`/pharmacodynamics` | String | * |
`mechanismOfAction` | `/mechanism-of-action` | String | 1 |
`overdosage` | `/toxicity` | String | 1 |
`labelDetails` | `/fda-label` | URL | 1 |
`foodWarning` | `/food-interactions/food-interaction` | String | * |
`warning` | `/indication` | String | 1 |
`interactingDrug` | `/drug-interactions/drug-interaction` | [Drug](https://health-lifesci.schema.org/Drug) | * | A persistent identifier will be included to refer to an existing resource
`sameAs` | `/external-links/external-link` | URL | * |
`subjectOf` | `/general-references/articles` | [MedicalScholarlyArticle](https://health-lifesci.schema.org/MedicalScholarlyArticle) | * | A persistent identifier will be included to refer to an existing resource

We implemented the data mapping using the [JSLT](https://github.com/schibsted/jslt) syntax and you can find the detail in _./src/main/resources/drugbank.jslt_ file.

## Example Output

The example below shows the result of executing the pipeline against a data record for [Mafenide](https://www.drugbank.ca/drugs/DB06795) in the DrugBank site.
```
{
  "@context": "http://schema.org",
  "@type": "Drug",
  "@id": "http://identifiers.org/drugbank/DB06795",
  "name": "Mafenide",
  "description": "Mafenide is a sulfonamide-type medication used to treat severe burns. It acts by reducing the bacterial population present in the burn tissue and promotes healing of deep burns. ",
  "alternateName": [
    "4-(aminomethyl)benzenesulfonamide",
    "4-Homosufanilamide",
    "Bensulfamide",
    "Maphenidum"
  ],
  "identifier": [
    {
      "@type": "PropertyValue",
      "name": "UNII",
      "value": "58447S8P4L"
    },
    {
      "@type": "PropertyValue",
      "name": "Drugs Product Database (DPD)",
      "value": "11904"
    },
    {
      "@type": "PropertyValue",
      "name": "PubChem Substance",
      "value": "310264891"
    },
    {
      "@type": "PropertyValue",
      "name": "ChEMBL",
      "value": "CHEMBL419"
    },
    {
      "@type": "PropertyValue",
      "name": "KEGG Drug",
      "value": "D02351"
    },
    {
      "@type": "PropertyValue",
      "name": "Wikipedia",
      "value": "Mafenide"
    },
    {
      "@type": "PropertyValue",
      "name": "CAS",
      "value": "138-39-6"
    },
    {
      "@type": "PropertyValue",
      "name": "PubChem Compound",
      "value": "3998"
    }
  ],
  "drugClass": "Benzenesulfonamides",
  "activeIngredient": [
    "Mafenide Acetate"
  ],
  "clinicalPharmacology": [
    "ABSORPTION: Mafenide is absorbed through devascularized areas into the systemic circulation following topical administration.",
    "METABOLISM: Metabolized to a carbonic anhydrase inhibitor.",
    "ROUTE OF ELIMINATION: Renal—Metabolite rapidly excreted in urine in high concentrations; however, the parent compound has not been detected in urine. ",
    "HALF-LIFE: N/A",
    "CLEARANCE: N/A",
    "VOLUME OF DISTRIBUTION: N/A",
    "AFFECTED ORGANISMS: Gram negative and gram positive bacteria; Bacteria; Pseudomonas aeruginosa",
    "PHARMACODYNAMICS: N/A"
  ],
  "mechanismOfAction": "The precise mechanism of mafenide is unknown. However, mafenide reduces the bacterial population in the avascular burn tissue and promotes spontaneous heeling of deep burns. ",
  "overdosage": "Since mafenide is metabolized to a carbonic anhydrase inhibitor, metabolic acidosis could occur. Mafenide is contraindicated in persons with renal impairment or sulfonamide hypersensitivity.",
  "labelDetails": "https://s3-us-west-2.amazonaws.com/drugbank/fda_labels/DB06795.pdf?1400780957",
  "warning": "Mafenide is indicated in the treatment of severe burns.",
  "interactingDrug": [
    {
      "@type": "Drug",
      "@id": "http://identifiers.org/drugbank/DB00250",
      "name": "Dapsone",
      "warning": "The risk or severity of adverse effects can be increased when Dapsone is combined with Mafenide."
    },
    {
      "@type": "Drug",
      "@id": "http://identifiers.org/drugbank/DB09214",
      "name": "Dexketoprofen",
      "warning": "The risk or severity of adverse effects can be increased when Dexketoprofen is combined with Mafenide."
    },
    {
      "@type": "Drug",
      "@id": "http://identifiers.org/drugbank/DB00657",
      "name": "Mecamylamine",
      "warning": "The risk or severity of adverse effects can be increased when Mafenide is combined with Mecamylamine."
    },
    {
      "@type": "Drug",
      "@id": "http://identifiers.org/drugbank/DB00435",
      "name": "Nitric Oxide",
      "warning": "The risk or severity of adverse effects can be increased when Nitric Oxide is combined with Mafenide."
    },
    {
      "@type": "Drug",
      "@id": "http://identifiers.org/drugbank/DB00750",
      "name": "Prilocaine",
      "warning": "The risk or severity of adverse effects can be increased when Mafenide is combined with Prilocaine."
    },
    {
      "@type": "Drug",
      "@id": "http://identifiers.org/drugbank/DB09112",
      "name": "Sodium Nitrite",
      "warning": "The risk or severity of adverse effects can be increased when Mafenide is combined with Sodium Nitrite."
    }
  ],
  "sameAs": [
    "http://www.drugs.com/international/Mafenide.html"
  ],
  "subjectOf": [
    {
      "@type": "MedicalScholarlyArticle",
      "@id": "http://identifiers.org/pubmed/22396671"
    }
  ],
  "code": {
    "@type": "MedicalCode",
    "@id": "http://identifiers.org/mesh/D008272",
    "codeValue": "D008272",
    "codingSystem": "MESH"
  }
}
```

## Related Resources

Here you can find several related resources used to construct the data mapping:
* https://health-lifesci.schema.org/Drug: The Schema.org specification for Drug class.
* https://www.drugbank.ca/releases/latest: The complete downloadable DrugBank data set in XML format
* https://www.drugbank.ca/docs/drugbank.xsd: XML Schema for DrugBank public XML data set

The DrugBank data sets are released under a [Creative Common's Attribution-NonCommercial 4.0 International License](https://creativecommons.org/licenses/by-nc/4.0/legalcode). They can be used freely in your non-commercial application or project.