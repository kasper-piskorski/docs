---
title: An Example of Migrating CSV to Grakn
keywords: migration
last_updated: January 2017
tags: [migration, examples]
summary: "An example to illustrate migration of CSV to Grakn"
sidebar: documentation_sidebar
permalink: /documentation/examples/CSV-migration.html
folder: documentation
comment_issue_id: 42
---


## Introduction

This example looks at the migration of genealogy data in CSV format to build a graph in GRAKN.AI. The data is used as the basis of a [blog post](https://blog.grakn.ai/family-matters-1bb639396a24#.2e6h72y0m) that illustrates the fundamentals of the Grakn visualiser, reasoner and analytics components. 

As the blog post explains, the original data was a [document](http://www.lenzenresearch.com/titusnarrlineage.pdf) from [Lenzen Research](http://www.lenzenresearch.com/) that described the family history of Catherine Niesz Titus for three generations of her maternal lineage.

In this example, we will walk through how to migrate the CSV data into Grakn, and confirm that we have succeeded using the Grakn visualiser. 

## Genealogy Data

The data for this example can be found as a set of CSV files in Grakn's [sample-datasets](https://github.com/graknlabs/sample-datasets) repository on Github. The data was put together by our team from narrative information gleaned from the original Lenzen Research [document](http://www.lenzenresearch.com/titusnarrlineage.pdf), with some minor additions to generate some interesting queries for Grakn's reasoner.

Let's take a look at the *raw-data* directory in the [genealogy-graph repo](https://github.com/graknlabs/sample-datasets/tree/master/genealogy-graph), which contains five CSV files. These files were put together by hand by our team, mostly by [Michelangelo](https://blog.grakn.ai/@doctormiko).

|filename|description|
|[*people.csv*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/people.csv)| This comprehensive CSV contains information about all the individuals discussed in the Lenzen document. For each row, it lists the available information about individuals' names, gender, birth and death dates and age at death. It also assigns each person a person ID ("pid"), which is a string containing the full name of each individual, and is used for identification of individuals in the other CSV files.|
|[*births.csv*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/births.csv)| This CSV lists the person IDs of a child and each of its parents|
|[*weddings.csv*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/weddings.csv)| This CSV comprises a row for each marriage, identifying each by a wedding ID (wid). The rows contain the person IDs of each spouse and the date of their wedding, where it is known.|
|[*documents.csv*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/documents.csv)| This CSV contains rows for each piece of documentary evidence used, and identifies them using a document ID. For each is recorded the type of document, its URL and description in the form of notes|
|[*evidences.csv*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/evidences.csv)| This CSV lists notable events that can be gleaned from the documentary evidence, and records each as a separate row. For each is noted the associated document ID, function (i.e. what happened), and the protagonists' person ID(s).|


## Example Ontology

On GRAKN.AI, the first step when working with a new dataset is to define its ontology in Graql. The ontology is a way to describe the entities and their relationships, so the underlying graph can store them as nodes and edges. You can find out more in our guide to the Grakn Knowledge Model. The ontology allows Grakn to perform:

* logical reasoning over the represented knowledge, such as the extraction of implicit information from explicit data (inference)
* discovery of inconsistencies in the data (validation).

The complete ontology for the genealogy-graph demo is in our sample-datasets repository, named [ontology.gql](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/ontology.gql). However, it is a bit more complex that we need for this example, because it contains definitions for what may be inferred from the CSV data, using some inference rules, by the Grakn reasoner. 

So, for this migration example, we will instead use *basic-ontology.gql*, which is also on the sample-datasets repository. It is a simplified ontology which represents the data imported from the CSV files described above. 

### Entities and Resources

The ontology contains the following entities:

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

### Relations and Roles

The ontology also describes the relations between entities, namely:
 
* the `event-protagonist` relation, which links events to people 
		- The role of the person in the event is described by a resource named `function` (which is of datatype string and indicate newborn, spouse, parent or deceased).
		- Has two associated roles: `happening` (played by an `event` entity such as a `birth` or `wedding`) and `protagonist` (played by a `person` entity)
 		

* the `conclusion-evidence` relation, which links documentary evidence to an event
		- Has two associated roles `evidence` (played by a `document`) and `conclusion` (played by an event such as a `wedding`).

Note that in this example, no relations between `person` entities are described. The inference of such family relationships is discussed fully in a separate example that covers [inference using the Grakn reasoner](./grakn-reasoner.html).

To load *basic-ontology.gql* into Grakn, make sure the engine is running and choose a clean keyspace in which to work (in the example below we use the default keyspace, so we are cleaning it before we get started). Pull down the [sample-datasets repo](https://github.com/graknlabs/sample-datasets), and call the following from the terminal, from within the *genealogy-graph* directory:

```bash
<relative-path-to-Grakn>/bin/grakn.sh clean
<relative-path-to-Grakn>/bin/grakn.sh start
<relative-path-to-Grakn>/bin/graql.sh -f ./basic-ontology.gql
```
		
## Migration Templates

Having loaded the ontology, the next steps are to populate the graph by migrating data into Grakn from the CSV files we discussed earlier. For a detailed overview of CSV migration, we recommend that you take a look at the Grakn documentation on [CSV Migration](https://grakn.ai/pages/documentation/migration/CSV-migration.html) and [Graql templating](https://grakn.ai/pages/documentation/graql/graql-templating.html).  

In essence, we need to write a set of template Graql statements that instruct the Grakn migrator on how the CSV data can be mapped to the ontology. The Grakn migrator applies the template to each row of data in a CSV file, replacing the indicated sections in the template with the value from a specific cell, identified by the column header (the key). This sounds complicated, but isn't really, as we will show.

Let's take a look at [*people-migrator.gql*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/migrators/people-migrator.gql), which is inside the */raw-data/migrators/* directory of the genealogy-graph project. The template inserts person entities, birth entities, death entities and the corresponding event-protagonist relationships with function newborn and deceased. The code is applied row-by-row to the [*people.csv*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/people.csv) file. The first section is as follows:

```graql-template
insert
	$p isa person has identifier <pid>;
```

For each row in the CSV file, the code above inserts a `person` entity with an `identifier` resource that takes the value of the cell in that row within the column with header `pid`.  The resultant Graql insert statement may look something like this:

```graql
insert $p0 isa person has identifier "Elizabeth Niesz";
```

The following code is slightly more complex. It inserts a `birth` event entity, and from the row in the CSV file, takes values from:

* the `name1` column for the `firstname` resource
* the `gender` column for the `gender` resource
* the `surname` column for the `surname` resource, if a value exists
* the `name2` column for the `middlename` resource, if a value exists
* the `born` column for the `birth-date` resource, if a value exists

The code also creates an `event-protagonist` relationship between the `person` and the `birth` event, giving the relationship a `function` resource string `newborn`.


```	graql-template
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

The resultant Graql insertion looks as follows, for a single row of the CSV file:

```graql
$x0 has confidence "high" isa birth has gender "female" has firstname "Elizabeth";
(happening: $x0, protagonist: $p0) has function "newborn" isa event-protagonist;
$x0 has surname "Niesz";
$x0 has birth-date "1820-08-27";

```

The code continues to similarly create a `death` event entity, and from the row in the CSV file, takes values from:

* the `dead` column for the `death-date` resource, if a value exists
* the `age` column for the `age` resource, if a value exists

The code also creates an `event-protagonist` relationship between the `person` and the `death` event, giving the relationship a `function` resource string `deceased`.


```graql-template
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

The resulting Graql insertion code looks something like the following for a row from the CSV file:

```graql
$y0 has confidence "high" isa death;
isa event-protagonist has function "deceased" (happening: $y0, protagonist: $p0);
$y0 has death-date "1891-12-08";
$y0 has age 71;
```

{% include note.html content="**A note on the `confidence` resource**   <br />
The `confidence` resource associated with the `event` entity can be set to 'high', 'medium' or 'low'. It is not used in this example, but is used by GRAKN.AI when the reasoner makes inference about the data, and will be explained further in an upcoming example. The `confidence` is set to 'high' in this example because all additions to the graph come from CSV data migration rather than through inference, so we can be confident that they are correct." %}

Moving on to look at [*births-migrator.gql*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/migrators/births-migrator.gql), which is applied row-by-row to the [*births.csv*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/births.csv) file.

```graql-template
match
	$c has identifier <child>;
	$p1 has identifier <parent1>;
	$p2 has identifier <parent2>;
	$b isa birth;
	$r (happening: $b, protagonist: $c) isa event-protagonist;
	$r has function "newborn";
insert
	$r1 (happening: $b, protagonist: $p1) isa event-protagonist
		has function "parent";
	$r2 (happening: $b, protagonist: $p2) isa event-protagonist,
		has function "parent";
```

For each row, the code matches the `person` entities identified by their names in the child, parent1 and parent2 columns of the CSV data. It then finds the `event-protagonist` relationship with the `function` resource set to be `newborn` that corresponds to the named child, which was added to Grakn by the people migrator. For the `birth` event associated with that relationship, it then inserts two new `event-protagonist` relationships between the `birth` and each of the `person` entities who are parents. Each of these new relationships have their `function` resource set to be `parent`.

From a single row of the CSV data, an example Graql insertion looks as follows:

```graql
match $p10 has identifier "Elizabeth Niesz"; $r0 has function "newborn"; $b0 isa birth; $r0 (happening: $b0, protagonist: $c0) isa event-protagonist; $c0 has identifier "Mary Melissa Titus"; $p20 has identifier "William Sanford Titus";
insert $r10 has function "parent" isa event-protagonist (happening: $b0, protagonist: $p10);
$r20 has function "parent" isa event-protagonist (happening: $b0, protagonist: $p20);
```

Moving on to look at the [*weddings-migrator.gql*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/migrators/weddings-migrator.gql), which is applied row-by-row to the [*weddings.csv*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/weddings.csv).

```graql-template
match
	$x has identifier <spouse1>;
	$y has identifier <spouse2>;
insert
	$m isa wedding,
	has identifier <wid>,
	has confidence "high";
	if (<date> != null) do 
		{
		$m has date <date>;
		}
	(happening: $m, protagonist: $x) isa event-protagonist,
		has function "spouse";
	(happening: $m, protagonist: $y) isa event-protagonist,
		has function "spouse";
```

The code matches the `person` entities identified by their names in the spouse1 and spouse2 columns of the CSV data. For each row, it insert a `wedding` entity and an `event-protagonist` relationship for each spouse. An example of a Graql insertion for a single line of CSV data is as follows:

```graql
match $y0 has identifier "William Sanford Titus"; $x0 has identifier "Elizabeth Niesz";
insert $m0 isa wedding has confidence "high" has identifier "w01";
$m0 has date "1845-10-21";
(happening: $m0, protagonist: $x0) has function "spouse" isa event-protagonist;
has function "spouse" isa event-protagonist (happening: $m0, protagonist: $y0);
```

The [*documents-migrator.gql*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/migrators/documents-migrator.gql) is applied to each row of the [*documents.csv*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/documents.csv) file. It is a very simple template that inserts a `document` entity for each row of data.

```graql-template
insert
	$d isa document,
		has document-type <document-type>,
		has document-location <url>,
		has identifier <did>,
		has notes <notes>;
``` 

The generated Graql statements for document migration are as follows for the a single row of CSV data:

```graql
insert $d0 has document-type "picture" has identifier "d01" has document-location "http:\/\/www.christliebfamilyassociation.com\/images\/CatherineNieszChristlieb.jpg" has notes "Dated Picture of Catherine Niesz" isa document;
```

Finally, [*evidences-migrator.gql*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/migrators/evidences-migrator.gql) is applied row-by-row to [*evidences.csv*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/raw-data/evidences.csv). 

```graql-template
match 
	$d isa document has identifier <document>;
	$p1 isa person has identifier <protagonist1>;
	$r1 (happening: $e, protagonist: $p1); 
		$r1 has function <function>;
	if (<protagonist2> != "") do {
	$p2 isa person has identifier <protagonist2>;
	$r2 (happening: $e, protagonist: $p2);
		$r2 has function <function>;
	}
insert
	(evidence: $d, conclusion: $e) isa conclusion-evidence;
```

For each row, the template matches a `document` by the identifier in the document column, and likewise matches a `person` by the identifier in the protagonist1 column. It then matches an `event-protagonist` relationship according to the `person` and the `function` specified in the function column. If there is a value specified in the protagonist2 column, a second `person` and `event-protagonist` relationship are also matched). The template then inserts a `conclusion-evidence` relationship, using the `event` matched with the `event-protagonist` relationship(s) to be the conclusion and the matched `document` as the evidence.

The Graql generated by Grakn's `migration.sh` looks as follows, for a single row of CSV data:

```graql
match $p10 isa person has identifier "Catherine Niesz"; $d0 has identifier "d01" isa document; $r10 (happening: $e0, protagonist: $p10); $r10 has function "deceased";
insert isa conclusion-evidence (evidence: $d0, conclusion: $e0);
```

## Migration Script

To invoke each of the migration templates on the appropriate .CSV file, you can call the Grakn migrator `migration.sh`, which resides in the */bin/* directory of the Grakn installation. Usage instructions are available as part of our [migration documentation](../migration/migration-overview.html). 

However, for simplicity, the */raw-data/* directory of the genealogy-graph project contains a script called *loader.sh* that calls each migration script in turn, so you can simply call the script from the terminal, from within the genealogy-graph directory, passing in the path to the Grakn */bin/* directory.

```bash
 ./loader.sh <location-of-Grakn-on-your-computer>/bin
```

The migration will take a minute or two, and the terminal will report which file it is migrating at each step. When it is complete, it will report that it is "Done migrating data". To check that it has been successful, open the [Grakn visualiser](../the-basics/visualiser.html) and select Types, then Entities, and choose one of those presented to you (the entities should be those described [above](./CSV-migration.html#entities)]. The visualiser will display the entities it has imported. The screenshot below illustrates the result from selecting to see all `person` entities.

![Person query](/images/match-$x-isa-person.png)

We have completed the data import, and the graph can now be queried. Since we have used just the basic ontology for this example, it isn't possible to perform the inference that is described in this blog post, that uses the same data.


## Where Next?

This example has illustrated how to migrate CSV data into Grakn. Having read it, you may want to further study our documentation about [CSV migration](../migration/CSV-migration.html) and [Graql templating](https://grakn.ai/pages/documentation/graql/graql-templating.html).  

We will be using the genealogy data throughout our documentation, and will shortly add further examples to illustrate the use of Grakn's inference and analytics, but as a starting point, please see our [Family Matters](https://blog.grakn.ai/family-matters-1bb639396a24#.uelgekrn2) blog post.

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/42" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
