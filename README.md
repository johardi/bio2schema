# Bio2Schema

## Overview
The Bio2Schema Project makes public health data on the Web available in [Schema.org](https://schema.org/) format.

In this pilot project, we focus on 3 biomedical data repositories, which are:
1. [ClinicalTrials.gov](https://clinicaltrials.gov/)
2. [PubMed](https://www.ncbi.nlm.nih.gov/pubmed/)
3. [DrugBank](https://www.drugbank.ca/)

and implement a data transformation pipeline for each data repository so that every data records are written in the Schema.org format.

## Sample Run
To run the pipeline, first clone this project and execute `gradle runApp` in the command line

```
$ git clone https://github.com/johardi/bio2schema.git
$ cd bio2schema/client-app
```

* Transforming the ClinicalTrials.gov data
```
$ gradle runApp --args='ClinicalTrials ./data/NCT00221338.xml ./output'
```

* Transforming the PubMed data
```
$ gradle runApp --args='PubMed ./data/PM27651978.xml ./output'
```

* Transforming the DrugBank data
```
$ gradle runApp --args='DrugBank ./data/DB06795.xml ./output'
```

_Note: The application supports a concurrent batch processing by adding a number of thread argument (e.g., `--args='ClinicalTrials ./data ./output 4'`)_

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