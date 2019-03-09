# Bio2Schema

## Overview
The Bio2Schema Project makes public health data on the Web available in [Schema.org](https://schema.org/) format.

In this pilot project, I'm sourcing the data from 3 public biomedical data repositories, which are
1. [ClinicalTrials.gov](https://clinicaltrials.gov/): a database of privately and publicly funded clinical studies conducted around the world
2. [PubMed](https://www.ncbi.nlm.nih.gov/pubmed/): a free search engine accessing primarily the MEDLINE database of references and abstracts on life sciences and biomedical topics
3. [DrugBank](https://www.drugbank.ca/): a comprehensive, freely accessible, online database containing information on drugs and drug targets

and for each repository, I implemented an ETL pipeline that will transform each data record to a corresponding Schema.org type specification ([HERE](https://github.com/johardi/bio2schema/blob/develop/bio2schema-pipeline-clinicaltrials/README.md), [HERE](https://github.com/johardi/bio2schema/blob/develop/bio2schema-pipeline-pubmed/README.md), and [HERE](https://github.com/johardi/bio2schema/blob/develop/bio2schema-pipeline-drugbank/README.md))

## Sample Run
To run the pipeline, first clone this project and execute `gradle runApp` in the command line

```
$ git clone https://github.com/johardi/bio2schema.git
$ cd bio2schema/client-app
```

* Transforming a single ClinicalTrials.gov data record
```
$ gradle runApp --args='ClinicalTrials ./data/NCT00221338.xml ./output-dir'
```

* Transforming a single PubMed data record
```
$ gradle runApp --args='PubMed ./data/PM27651978.xml ./output-dir'
```

* Transforming a single DrugBank data record
```
$ gradle runApp --args='DrugBank ./data/DB06795.xml ./output-dir'
```

The application also supports a concurrent batch processing and you can enable it by adding a number of thread argument in the command line
```
$ gradle runApp --args='ClinicalTrials ./input-dir ./output-dir 4'
```

## License
This software is licensed under the Apache 2 license, quoted below.

```
Copyright (c) 2019 Josef Hardi <josef.hardi@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```
