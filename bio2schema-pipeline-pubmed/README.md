# Schema.org Transformation for PubMed Data

This folder contains a data transformation pipeline that will transform [PubMed](https://www.ncbi.nlm.nih.gov/pubmed/) data into Schema.org's structure.

The pipeline will transform the original data format from XML to JSON-LD following the [MedicalScholarlyArticle](https://health-lifesci.schema.org/MedicalScholarlyArticle) specification. Additionally, the pipeline will reconcile names with known LOD databases (e.g., DBpedia and BioPortal) to find their persistent identifier and preferred name so that it is possible to link the information with other well-known resources.

## Data Mapping

The table below shows the data mapping used to transform DrugBank XML data to Schema.org specification.
* The `Schema.org Data Element` column shows the data field name in the Schema.org specification
* The `PubMed Data Element` column shows the XML path to denote the source value
* The `Type` column indicates the data type used to encapsulate the value
* The `Cardinality` column indicates the data structure used to store the value (i.e., 1 = a single value, * = an array, 1..* = either a single value or an array).
* Lastly, the `Note` column describes some additional information regarding the data transformation

| Schema.org Data Element | PubMed Data Element | Type | Cardinality | Notes
--- | --- | :---: | :---: | ---
`identifier` | `/PubmedData/ArticleIdList/ArticleId` | String | * |
`name` | `/MedlineCitation/Article/ArticleTitle` | String | 1 |
`description` | `/MedlineCitation/Article/Abstract/AbstractText` | String | 1 |
`disambiguatingDescription` | `/MedlineCitation/Article/Abstract/AbstractText` | String | * | For some publications, the description will be broken down into different parts, e.g., "OBJECTIVES", "METHODS", "RESULTS", and "CONCLUSIONS"
`publicationType` | `/MedlineCitation/Article/PublicationTypeList` | String | * |
`keywords` | `/MedlineCitation/KeywordList` | String | * |
`pagination` | `/MedlineCitation/Article/Pagination/MedlinePgn` | String | 1 |
`url` | `/MedlineCitation/Article/ELocationID` | URL | 1 |
`datePublished` | `/MedlineCitation/Article/ArticleDate` | Date | 1 | If the preferred element doesn't exist, the `/PubmedData/History/PubMedPubDate` is used instead
`inLanguage` | `/MedlineCitation/Article/Language` | String | 1 |
`author` | `/MedlineCitation/Article/AuthorList` | [Person](https://schema.org/Person) | * | An entity reconciler will be used to get the affiliation's entity id via [Dbpedia lookup service](https://wiki.dbpedia.org/lookup). The author's ORCID will also be extracted, if present
`isPartOf` | `/MedlineCitation/Article/Journal` | [Periodical](https://schema.org/Periodical),<br>[PublicationVolume](https://schema.org/PublicationVolume),<br>[PublicationIssue](https://schema.org/PublicationIssue) | 1 |
`funder` | `/MedlineCitation/Article/GrantList/Grant/Agency` | [Organization](https://schema.org/Organization) | * | An entity reconciler will be used to get the entity id via [Dbpedia lookup service](https://wiki.dbpedia.org/lookup)
`citation` | `/PubmedData/ReferenceList` | [MedicalScholarlyArticle](https://health-lifesci.schema.org/MedicalScholarlyArticle) | * | A persistent identifier will be included to refer to an existing resource
`about` | `/MedlineCitation/MeshHeadingList` | [MedicalEntity](https://health-lifesci.schema.org/MedicalEntity) | * | An entity reconciler will be used to get the medical code via [BioPortal search service](http://data.bioontology.org/documentation#nav_search)

We implemented the data mapping using the [JSLT](https://github.com/schibsted/jslt) syntax and you can find the detail in _./src/main/resources/pubmed.jslt_ file.

## Example Output

The example below shows the result of executing the pipeline against an article titled ["The Effect of Emotional Closeness and Exchanges of Support Among Family Members on Residents' Positive and Negative Psychological Responses After Hurricane Sandy"](https://www.ncbi.nlm.nih.gov/pmc/articles/PMC5016198) in the PubMed site.
```
{
  "@context": "http://schema.org",
  "@type": "MedicalScholarlyArticle",
  "@id": "http://identifiers.org/pubmed/27651978",
  "identifier": [
    {
      "@type": "PropertyValue",
      "name": "pubmed",
      "value": "27651978"
    },
    {
      "@type": "PropertyValue",
      "name": "doi",
      "value": "10.1371/currents.dis.5eebc1ace65be41d0c9816c93d16383b"
    },
    {
      "@type": "PropertyValue",
      "name": "pmc",
      "value": "PMC5016198"
    }
  ],
  "publicationType": [
    "Journal Article"
  ],
  "name": "The Effect of Emotional Closeness and Exchanges of Support Among Family Members on Residents' Positive and Negative Psychological Responses After Hurricane Sandy.",
  "description": "This study examines how changes in emotional closeness and exchanges of support among family members after Hurricane Sandy affected residents' psychological outcomes both [...]",
  "disambiguatingDescription": [
    "INTRODUCTION: This study examines how changes in emotional closeness and exchanges of support among family members [...]",
    "METHODS: The working sample included 130 family ties reported by 85 respondents recruited from community and shelter [...]",
    "RESULTS: Results showed psychological distress was significantly increased with higher levels of instrumental support [...]",
    "DISCUSSION: It is suggested that after a significant disaster, although a family may be the best to take care of its [...]"
  ],
  "url": "http://doi.org/10.1371/currents.dis.5eebc1ace65be41d0c9816c93d16383b",
  "datePublished": "2016-08-24",
  "inLanguage": "eng",
  "author": [
    {
      "@type": "Person",
      "givenName": "Zhen",
      "familyName": "Cong"
    },
    {
      "@type": "Person",
      "givenName": "Daan",
      "familyName": "Liang",
      "affiliation": {
        "@type": "EducationalOrganization",
        "name": "Texas Tech University",
        "@id": "http://dbpedia.org/resource/Texas_Tech_University",
        "alternateName": "Civil, Environmental, and Construction Engineering, Texas Tech University, Lubbock, Texas."
      }
    },
    {
      "@type": "Person",
      "givenName": "Ali",
      "familyName": "Nejat"
    }
  ],
  "isPartOf": {
    "@type": [
      "Periodical",
      "PublicationVolume",
      "PublicationIssue"
    ],
    "name": "PLoS currents",
    "alternateName": "PLoS Curr",
    "issn": "2157-3999",
    "volumeNumber": "8",
    "datePublished": "2016-08"
  }
}
```
_(Note: Some lengthy paragraphs in the example are truncated for a presentation purpose)_

## Related Resources

Here you can find several related resources used to construct the data mapping:
* https://health-lifesci.schema.org/MedicalScholarlyArticle: The Schema.org specification for MedicalScholarlyArticle class.
* ftp://ftp.ncbi.nlm.nih.gov/pubmed/baseline: The complete downloadable PubMed data set in XML format
* https://dtd.nlm.nih.gov/ncbi/pubmed/out/pubmed_190101.dtd: XML Schema for 	PubMed public XML data set (2019-01-01 edition)

The use of PubMed data is subject to these [Terms and Conditions](ftp://ftp.ncbi.nlm.nih.gov/pubmed/baseline/README.txt).
