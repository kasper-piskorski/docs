---
title: An Example of Migrating CSV to Grakn
keywords: migration
last_updated: January 2017
tags: [migration, examples]
summary: "A short example to illustrate migration of CSV to Grakn"
sidebar: documentation_sidebar
permalink: /documentation/examples/CSV-migration.html
folder: documentation
comment_issue_id: 42
---


## Introduction

This example looks at the migration of genealogy data in CSV format to build a graph in GRAKN.AI. The data is used as the basis of a [blog post](https://blog.grakn.ai/family-matters-1bb639396a24#.2e6h72y0m) that illustrates the fundamentals of the Grakn visualiser, reasoner and analytics components. 

As the blog post explains, the original data was a [document](http://www.lenzenresearch.com/titusnarrlineage.pdf) from [Lenzen Research](http://www.lenzenresearch.com/) that described the family history of Catherine Niesz Titus for three generations of her maternal lineage.

## Data

The data for this example can be found as a set of CSV files in Grakn's [sample-datasets](https://github.com/graknlabs/sample-datasets) repository on Github. The data was put together by our team from narrative information gleaned from the original Lenzen Research [document](http://www.lenzenresearch.com/titusnarrlineage.pdf), with some minor additions to facilitate some interesting queries for Grakn's reasoner.

Let's take a look at the raw-data directory in the [genealogy-graph repo](https://github.com/graknlabs/sample-datasets/tree/master/genealogy-graph), which contains five CSV files. These files were put together by hand by our team, mostly by [Michelangelo](https://blog.grakn.ai/@doctormiko).

|filename|description|
|[people.csv](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/people.csv)| This comprehensive CSV contains information about all the individuals discussed in the Lenzen document. For each row, it lists the available information about individuals' names, gender, birth and death dates and age at death. It also assigns each person a person ID ("pid"), which is a string containing the full name of each individual, and is used for identification of individuals in the other CSV files.|
|[births.csv](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/births.csv)| This CSV lists the person IDs of a child and each of its parents|
|[weddings.csv](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/weddings.csv)| This CSV comprises a row for each marriage, identifying each by a wedding ID (wid). The rows contain the person IDs of each spouse and the date of their wedding, where it is known.|
|[documents.csv](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/documents.csv)| This CSV contains rows for each piece of documentary evidence used, and identifies them using a document ID. For each is recorded the type of document, its URL and description in the form of notes|
|[evidences.csv](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/evidences.csv)| This CSV lists notable events that can be gleaned from the documentary evidence, and records each as a separate row. For each is noted the associated document ID, function (i.e. what happened), and the protagonists' person ID(s).|


## Ontology

On GRAKN.AI, the first step when working with a new dataset is to define its ontology in Graql. The ontology is a way to describe the entities and their relationships, so the underlying graph can store them as nodes and edges. You can find out more in our guide to the Grakn Knowledge Model. The ontology allows Grakn to perform:

* logical reasoning over the represented knowledge, such as the extraction of implicit information from explicit data (inference)
* discovery of inconsistencies in the data (validation).

The complete ontology for the genealogy-graph demo is in our sample-datasets repository, named [ontology.gql](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/ontology.gql). However, it is a bit more complex that we need for this example, because it contains definitions for what may be inferred from the CSV data, using some inference rules, by the Grakn reasoner. 

So, for this migration example, we will instead use *basic-ontology.gql*, which is also on the sample-datasets repository. It is a simplified ontology which represents the data imported from the CSV files described above. The ontology contains the following entities:


* `person` 
	 - Has a resource `identifier`, which is a string representing the pid described above for the *people.csv* file, and is the full name of the person.

* `birth`, which is a subclass of an `event` entity and adds the following string resources: 
 		- `firstname`
		- `middlename` (if available)
		- `surname` (if available)
		- `gender`
	 	- `birth-date` (if available)

* `death`, which is a subclass of an `event` entity, and adds the following resources:
 		- `death-date` (if available)
 		- `age` at death (if available), of datatype long
	 	
* `wedding`, which is a subclass of an `event` entity
 		
* `document`, which has the following string resources:
 		- `identifier`
 		- `document-location` a URL 
 		- `document-type` 
 		- `notes`

The ontology also contains the relationships between the entities, namely:
 
* the `event-protagonist` relationship
		- Has a resource `function` (which is of datatype string and indicate newborn, spouse, parent or deceased).
		- Has two associated roles: `happening` (played by an `event` entity such as a `birth` or `wedding`) and `protagonist` (played by a `person` entity)
 		

Finally, there is another relationship, which is between `document` and `event` entities:

* conclusion-evidence relationship
		- Has two associated roles `evidence` (played by a `document`) and `conclusion` (played by an event such as a `wedding`).

To load *basic-ontology.gql* into Grakn, make sure the engine is running and choose a clean keyspace in which to work (in the exapmle below we use the default keyspace). Pull genealogy-graph from the sample-datasets repo, and call the following from the terminal, within the Grakn installation directory:

```bash
./bin/grakn.sh clean
./bin/grakn.sh start
./bin/graql.sh -f ../sample-datasets/genealogy-graph/basic-ontology.gql
```
		
## Migration

Having loaded the ontology, the next steps are to populate the graph by migrating data into Grakn from the CSV files we discussed earlier. For a detailed overview of CSV migration, we recommend that you take a look at the Grakn documentation on [CSV Migration](https://grakn.ai/pages/documentation/migration/CSV-migration.html) and [Graql templating](https://grakn.ai/pages/documentation/graql/graql-templating.html).  

In essence, we need to write a set of template Graql statements that instruct the Grakn migrator on how the CSV data can be mapped to the ontology. The Grakn migrator applies the template to each row of data in a CSV file, replacing the indicated sections in the template with the value from a specific cell, identified by the column header (the key). This sounds complicated, but isn't really, as we will show.

Let's take a look at *people-migrator.gql*, which is inside the raw-data/migrators/ directory of the genealogy-graph project. The template inserts person entities, birth entities, death entities and the corresponding event-protagonist relationships with function newborn and deceased. The code is applied row-by-row to the [people.csv](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/people.csv) file. The first section is as follows:

```graql
insert
	$p isa person has identifier <pid>;
```

For each row in the CSV file, the code above inserts a `person` entity with an `identifier` resource that takes the value of the cell in that row within the column with header `pid`.  

The following code is slightly more complex. It inserts a `birth` event entity, and from the row in the CSV file, takes values from:

* the `name1` column for the `firstname` resource
* the `gender` column for the `gender` resource
* the `surname` column for the `surname` resource, if a value exists
* the `name2` column for the `middlename` resource, if a value exists
* the `born` column for the `birth-date` resource, if a value exists

The code also creates an `event-protagonist` relationship between the `person` and the `birth` event, giving the relationship a `function` resource string `newborn`.


```	graql
	$x isa birth has firstname <name1>,
		has gender <gender>,
		has confidence "high";
	(happening: $x, protagonist: $p) isa event-protagonist,
		has function "newborn";
	if (<surname> != "") do 
		{
		$x has surname <surname>;
		}
	if (<name2> != "") do 
		{
		$x has middlename <name2>;
		}
	if (<born> != "") do 
		{
		$x has birth-date <born>;
		}
```

The code continues to similarly create a `death` event entity, and from the row in the CSV file, takes values from:

* the `dead` column for the `death-date` resource, if a value exists
* the `age` column for the `age` resource, if a value exists

The code also creates an `event-protagonist` relationship between the `person` and the `death` event, giving the relationship a `function` resource string `deceased`.


```
	$y isa death has confidence "high";
	(happening: $y, protagonist: $p) isa event-protagonist,
			has function "deceased";
	if (<dead> != "") do 
		{
		$y 	has death-date <dead>;
		}

	if (<age> != "") do 
		{
		$y 	has age @long(<age>);
		}
		
```

{% include note.html content="**A note on the `confidence` resource**   <br />
START HERE

" %}

births-migrator.gql - inserts event-protagonist relationships between birth and person entities with function newborn/parent

weddings-migrator.gql - inserts wedding entities and event-protagonist relationships between wedding and person entities with function spouse

documents-migrator.gql - inserts document entities

evidences-migrator.gql - inserts conclusion-evidence relationships between documents and events




















## Where Next?



{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/42" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
