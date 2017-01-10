---
title: CSV Migration to Grakn
keywords: setup, getting started
last_updated: November 2nd 2016
tags: [migration]
summary: "This document will teach you how to migrate CSV data into Grakn."
sidebar: documentation_sidebar
permalink: /documentation/migration/CSV-migration.html
folder: documentation
comment_issue_id: 32
---

## Introduction
This tutorial shows you how to populate Grakn with CSV data. If you have not yet set up the Grakn environment, please see the [setup guide](../get-started/setup-guide.html).

## Migration Shell Script for CSV
The migration shell script can be found in `/bin` directory of your Grakn environment. Usage is specific to the type of migration being performed. For CSV:

```bash
usage: ./migration.sh csv -template <arg> -input <arg> [-help] [-no] [-separator <arg>] [-batch <arg>] [-uri <arg>] [-keyspace <arg>] [-v]

OPTIONS
 -b,--batch <arg>       number of rows to load at once
 -h,--help              print usage message
 -i,--input <arg>       input csv file
 -k,--keyspace <arg>    keyspace to use
 -n,--no                dry run - write to standard out
 -s,--separator <arg>   separator of columns in input file
 -t,--template <arg>    graql template to apply over data
 -u,--uri <arg>         uri to engine endpoint
 -v,--verbose           print counts of migrated data.
```

## CSV Migration Basics

There are limitations on the CSV format that prevent it from expressing the semantics of the data. Grakn cannot automatically migrate and derive an ontology for your data. To have the full benefit of a knowledge graph, you must write the ontology for your dataset.

CSV Migration makes heavy use of the Graql templating language. You will need a foundation in Graql templating before continuing, so please read through our [templating documentation](../graql/graql-templating.html) to find out more.

Once you have written an ontology for your dataset, you will template Graql statements that instruct the migrator on how your data can be mapped to your ontology. The CSV migrator will apply the template to each row of data in the CSV file. If you are familiar with the Graql templating language, you are aware that it replaces the indicated sections in the template with provided data: the column header is the key and the content of each row at that column the value.

## Example 1: Cars

Let's take a simple example. First, the CSV:

```csv
Year,Make,Model,Description,Price
1997,Ford,E350,"ac, abs, moon",3000.00
1999,Chevy,"Venture",,4900.00
1996,Jeep,Grand Cherokee,"MUST SELL! air, moon roof, loaded",4799.00
```

Here is the ontology for the example:   

```graql
insert

car sub entity
    has-resource name
    has-resource year
    has-resource description
    has-resource price;

name sub resource datatype string;
year sub resource datatype string;
description sub resource datatype string;
price sub resource datatype double;

```

And the Graql template:   

```graql-template
insert 

$x isa car 
    has name <Make>-<Model>
    has year <Year>
    has price @double(Price)
    if (ne Description null) do { has description <Description>};
```

The template will create a `car` entity for each row. It will attach `year` and `price` resources to each of these entities. If the `description` resource is present in the data, it will attach the approriate `description` to the `car`.

The template is applied to each row, and the resulting Graql statement, if printed out, looks like:

```graql
insert $x0 isa car has name "Ford" has description "ac, abs, moon" has price 3000.0 has year "1997";
insert $x0 isa car has name "Chevy" has price 4900.0 has year "1999";
insert $x0 isa car has description "MUST SELL! air, moon roof, loaded" has price 4799.0 has name "Jeep" has year "1996";
```

You will note that the second Graql insert is missing the `description` resource. This is because that value is not present in the data and the template uses an `if` statement to check if it exists.

### Separator

The `separator` option allows you to specify the column separator. With this we are able to migrate a wider range of formats, including TSV.

This file would be migrated in the same way as the previous example when you specify the `\t` separator:

```tsv
Year  Make  Model Description Price
1997  Ford  E350  "ac  abs   moon"  3000.00
1999  Chevy "Venture" ""  4900.00
1996  Jeep  Grand Cherokee  "MUST SELL!
air  moon roof   loaded"  4799.00
```

## Example 2: Genealogy

This example uses a simple genealogy example to demonstrate the creation of a graph by importing data from three CSV files. For simplicity, the following text uses some of the data from our larger [genealogy-graph](https://github.com/graknlabs/sample-datasets/tree/master/genealogy-graph) example, but it is subset of that dataset for the purposes of illustrating the migration process only.

### Ontology

A simple ontology is shown below. There is a single entity, `person`, which has a number of resources and can play various roles (`parent`, `child`, `spouse1` and `spouse2`) in two possible relations (`parentship` and `marriage`).

```graql
insert

# Entities

person sub entity
	plays-role parent
	plays-role child
	plays-role spouse1
	plays-role spouse2

	has-resource identifier
	has-resource firstname
	has-resource surname
	has-resource middlename
	has-resource picture
	has-resource age
	has-resource birth-date
	has-resource death-date
	has-resource gender;

# Roles and Relations

marriage sub relation
	has-role spouse1
	has-role spouse2
	has-resource picture;

spouse1 sub role;
spouse2 sub role;

parentship sub relation
	has-role parent
	has-role child;

parent sub role;
child sub role;

# Resources

identifier sub resource datatype string;
firstname sub resource datatype string;
surname sub resource datatype string;
middlename sub resource datatype string;
picture sub resource datatype string;
age sub resource datatype long;
birth-date sub resource datatype string;
death-date sub resource datatype string;
gender sub resource datatype string;
``` 


### Data Migration

We will consider three CSV files that contain data to migrate into Grakn. 

#### people.csv

The *people.csv* file contains details of the people that we will use to create seven `person` entities. Note that not all fields are available for each person, but at the very least, each row is expected to have the following:

* pid (this is the person identifier, and is a string representing their full name)
* first name
* gender

```csv
name1,name2,surname,gender,born,dead,pid,age,picture
Timothy,,Titus, male,,,	Timothy Titus,,	
Mary,,Guthrie,female,,,Mary Guthrie,,	
John,,Niesz,male,1798-01-02,1872-03-06,John Niesz,74,
Mary,,Young,female,1798-04-09,1868-10-28,Mary Young,70,
William,Sanford,Titus,male,1818-03-23,01/01/1905,William Sanford Titus,76,
Elizabeth,,Niesz,female,1820-08-27,1891-12-08,Elizabeth Niesz,71,
Mary,Melissa,Titus,female,1847-08-12,10/05/1946,Mary Melissa Titus,98,
```

The Graql template code for the Grakn migrator is as follows:

```graql-template
insert
	$p isa person has identifier <pid>
		has firstname <name1>,
		
		if (<surname> != "") do 
		{
		has surname <surname>,
		}

		if (<name2> != "") do 
		{
		has middlename <name2>,
		}

		if (<picture> != "") do 
		{
		has picture <picture>,
		}

		if (<age> != "") do 
		{
		has age @long(<age>),
		}

		if (<born> != "") do 
		{
		has birth-date <born>,
		}

		if (<dead> != "") do 
		{
		has death-date <dead>,
		}

		has gender <gender>;
```

For each row in the CSV file, the template inserts a `person` entity with resources that take the value of the cells in that row. Where data is optional, the template checks to see if it is present before adding the resources for middlename, surname, picture, age, birth and death dates. 

Calling the Grakn migrator on the *people.csv* file using the above template (named `people-migrator.gql`) is performed as follows:

```bash
./<grakn-install-location>/bin/migration.sh csv -i ./people.csv -t ./people-migrator.gql
```

The data insertion generated by the migrator is as follows:

```graql
insert $p0 has death-date "1891-12-08" isa person has gender "female" has identifier "Elizabeth Niesz" has surname "Niesz" has age 71 has firstname "Elizabeth" has birth-date "1820-08-27";
insert $p0 has identifier "William Sanford Titus" has age 76 isa person has firstname "William" has surname "Titus" has gender "male" has birth-date "1818-03-23" has middlename "Sanford" has death-date "1905-01-01";
insert $p0 isa person has surname "Titus" has firstname "Timothy" has identifier "Timothy Titus" has gender "male";
insert $p0 isa person has firstname "Mary" has identifier "Mary Guthrie" has surname "Guthrie" has gender "female";
insert $p0 isa person has firstname "Mary" has death-date "1946-05-10" has surname "Titus" has age 98 has identifier "Mary Melissa Titus" has middlename "Melissa" has gender "female" has birth-date "1847-08-12";
insert $p0 has death-date "1872-03-06" has age 74 has identifier "John Niesz" isa person has birth-date "1798-01-02" has firstname "John" has gender "male" has surname "Niesz";
insert $p0 has identifier "Mary Young" has birth-date "1798-04-09" isa person has firstname "Mary" has death-date "1868-10-28" has surname "Young" has gender "female" has age 70;
```

#### births.csv

Each row of *births.csv* records a parent and child, with two rows for each of the three children listed:

```csv
parent,child
Timothy Titus,William Sanford TitusMary Guthrie,	William Sanford Titus
John Niesz,Elizabeth NieszMary Young,Elizabeth Niesz
Elizabeth Niesz,Mary Melissa TitusWilliam Sanford Titus,Mary Melissa Titus
```

The Graql template code for the Grakn migrator is as follows:

```graql-template
match
	$c isa person has identifier <child>;
	$p isa person has identifier <parent>;
insert
	(child: $c, parent: $p) isa parentship;

```

For each row in the CSV file, the template matches the child and parent cells to their corresponding `person` entities, and then inserts a `parentship` relation, placing the entities it has matched into the `child` and `parent` roles.

{% include note.html content="You must ensure that all entities exist in a graph before inserting relations." %}

The data insertion generated by the migrator is as follows:

```graql
match $c0 has identifier "William Sanford Titus" isa person; $p0 isa person has identifier "Timothy Titus";
insert (child: $c0, parent: $p0) isa parentship;
match $p0 isa person has identifier "Mary Guthrie"; $c0 has identifier "William Sanford Titus" isa person;
insert (child: $c0, parent: $p0) isa parentship;
match $p0 isa person has identifier "Elizabeth Niesz"; $c0 isa person has identifier "Mary Melissa Titus";
insert (child: $c0, parent: $p0) isa parentship;
match $c0 isa person has identifier "Mary Melissa Titus"; $p0 has identifier "William Sanford Titus" isa person;
insert (child: $c0, parent: $p0) isa parentship;
match $c0 isa person has identifier "Elizabeth Niesz"; $p0 has identifier "John Niesz" isa person;
insert (child: $c0, parent: $p0) isa parentship;
match $c0 isa person has identifier "Elizabeth Niesz"; $p0 has identifier "Mary Young" isa person;
insert (child: $c0, parent: $p0) isa parentship;
```

#### weddings.csv

The *weddings.csv* file contains two columns that correspond to both spouses in a marriage, and an optional column for a photograph of the happy couple:

```csv
spouse1,spouse2,pictureTimothy Titus,Mary Guthrie,John Niesz,Mary Young,http://1.bp.blogspot.com/-Ty9Ox8v7LUw/VKoGzIlsMII/AAAAAAAAAZw/UtkUvrujvBQ/s1600/johnandmary.jpgElizabeth Niesz,William Sanford Titus,
```

The Graql template code for the migrator is as follows:

```graql-template
match
	$x has identifier <spouse1>;
	$y has identifier <spouse2>;
insert
	(spouse1: $x, spouse2: $y) isa marriage

	if (<picture> != "") do 
		{
		has picture <picture>
		};
```

For each row in the CSV file, the template matches the two spouse cells to their corresponding `person` entities, and then inserts a `marriage` relation, placing the entities it has matched into the `spouse1` and `spouse2` roles. If there is data in the picture cell, a `picture` resource is also created for the `marriage` relation.

The Graql insertion code is as follows:

```graql
match $x0 has identifier "Timothy Titus"; $y0 has identifier "Mary Guthrie";
insert (spouse1: $x0, spouse2: $y0) isa marriage;
match $x0 has identifier "John Niesz"; $y0 has identifier "Mary Young";
insert has picture "http:\/\/1.bp.blogspot.com\/-Ty9Ox8v7LUw\/VKoGzIlsMII\/AAAAAAAAAZw\/UtkUvrujvBQ\/s1600\/johnandmary.jpg" (spouse1: $x0, spouse2: $y0) isa marriage;
match $y0 has identifier "William Sanford Titus"; $x0 has identifier "Elizabeth Niesz";
insert (spouse1: $x0, spouse2: $y0) isa marriage;
```


## Where Next?
We have an additional, more extensive, example of [migrating genealogy data from CSV](../examples/CSV-migration.html). Our [sample-projects repository on Github](https://github.com/graknlabs/sample-projects) also contains [an example that takes a simple CSV data file of pets](https://github.com/graknlabs/sample-projects/tree/master/example-csv-migration), and another [example for video games](https://github.com/graknlabs/sample-projects/tree/master/example-csv-migration-games), as described in a separate [blog post](https://blog.grakn.ai/twenty-years-of-games-in-grakn-14faa974b16e#.do8tq0dm8).

You can find further documentation about migration in our API reference documentation (which is in the `/docs` directory of the distribution zip file, and also online [here](https://grakn.ai/javadocs.html). 

{% include links.html %}


## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/32" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
