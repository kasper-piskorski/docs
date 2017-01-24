---
title: Quickstart Tutorial
keywords: setup, getting started
last_updated: December 2016
tags: [getting-started, graql]
summary: "This document will work through a simple example using the Graql shell to show how to get started with GRAKN.AI."
sidebar: documentation_sidebar
permalink: /documentation/get-started/quickstart-tutorial.html
folder: documentation
comment_issue_id: 17
---

## Introduction

In this guide, we are going to load a single file to set up a simple graph and insert some data. This example is loosely based on the genealogy-graph example that we use throughout our documentation. You can find the full example, including data and ontology, in the [sample-datasets repo on Github](https://github.com/graknlabs/sample-datasets/tree/master/genealogy-graph). However, we are not going to load the complete data and ontology here: this is a basic "quick start" example to illustrate a simple ontology and some basic data. 

If you have not yet set up GRAKN.AI, please see the [Setup guide](../get-started/setup-guide.html). 

## Using the Graql Shell

We will first make sure we are working with a clean 'keyspace'. From the terminal:

```bash
<relative-path-to-Grakn>/bin/grakn.sh clean
```

We then start the Grakn engine: 

```bash
<relative-path-to-Grakn>/bin/grakn.sh start
```

Finally, we load the ontology and data into Grakn, so we have a graph to work with, using the *basic-genealogy.gql* file stored in the *examples* directory of the Grakn distribution zip. You can also find this file on [Github](). Simply invoke the Graql shell passing the -f flag to indicate the file to load into a graph (if you are interested, please see our [documentation about flags supported by the Graql shell](../graql/graql-shell.html)):

```bash
<relative-path-to-Grakn>/bin/graql.sh -f <relative-path-to-Grakn>/examples/basic-genealogy.gql
```


### The Ontology

You can find out much more about the Grakn ontology in our documentation about the [Grakn knowledge model](../the-basics/grakn-knowledge-model.html), which states that

> "The ontology is a formal specification of all the relevant concepts and their meaningful associations in a given application domain. It allows objects and relationships to be categorised into distinct types, and for generic properties about those types to be expressed". 

For the purposes of this guide, you can think of the ontology as a schema that describes items of data and defines how they relate to one another.

There are a number of things we can say about ontology shown below:

* there is one entity, `person`, which represents a person in the family whose genealogy data we are studying. 
* the `person` entity has a number of resources to describe aspects of the person, such as their name, age, dates of birth and death, gender and a URL to a picture of them (if one exists). Those resources are all expressed as strings, except for the age, which is of datatype long.
* there are two relations that a `person` can participate in: `marriage` and `parentship`
* the person can play different roles in those relations, as a spouse (`spouse1` or `spouse2` - we aren't assigning them by gender to be husband or wife in this example) and as a `parent` or `child`.   
* the `marriage` relation has a resource, which is a URL to a wedding picture, if one exists. 

```graql
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
```

### The Data

The data is rather cumbersome, so we will not reproduce it all here. It is part of our [genealogy-graph](https://github.com/graknlabs/sample-datasets/tree/master/genealogy-graph) project, and you can find out much more about the Niesz family in our [CSV migration](../examples/CSV-migration.html) and [Graql reasoning](../examples/graql-reasoning.html) example documentation. Here is a snippet of some of the data that you added to the graph when you loaded the *basic-genealogy.gql* file:

```
$57472 isa person has firstname "Mary" has identifier "Mary Guthrie" has surname "Guthrie" has gender "female";
$86144 has surname "Dudley" isa person has identifier "Susan Josephine Dudley" has gender "female" has firstname "Susan" has middlename "Josephine";
$118912 has age 74 isa person has firstname "Margaret" has surname "Newman" has gender "female" has identifier "Margaret Newman";
...
$8304 (parent: $57472, child: $41324624) isa parentship;
$24816 (parent: $81976, child: $41096) isa parentship;
$37104 isa parentship (parent: $49344, child: $41127960);
...
$122884216 (spouse2: $57472, spouse1: $41406488) isa marriage;
$40972456 (spouse2: $40964120, spouse1: $8248) isa marriage;
$81940536 (spouse2: $233568, spouse1: $41361488) has picture "http:\/\/1.bp.blogspot.com\/-Ty9Ox8v7LUw\/VKoGzIlsMII\/AAAAAAAAAZw\/UtkUvrujvBQ\/s1600\/johnandmary.jpg" isa marriage;
```

Don't worry about the numbers such as `$57472`. These are variables in Graql, and happen to have randomly assigned numbers to make them unique. Each statement is adding either a `person`, a `parentship` or a `marriage` to the graph.  We will show how to add more data to the graph shortly in the Extending The Graph section. First, however, it is time to check the graph in the Graql shell. 

### Querying the Graph

To start the [Graql shell](../graql/graql-shell.html), type the following from the terminal:

```bash
<relative-path-to-Grakn>/bin/graql.sh
```

You will see a `>>>` prompt, at which point, you can make a number queries to explore the graph, as more fully described in the [Graql documentation](../graql/graql-overview.html). Here, we will make a couple of `match` queries:

Find all people in the graph, and list their `identifier` resources (a string that represents their full name):

```graql
match $p isa person, has identifier $i;
```

Find all people who are married:

```graql
match (spouse1: $x, spouse2: $y) isa marriage; $x has identifier $xi; $y has identifier $yi;  
```

<!-- JCS: Trying to show all parents ```match $x isa parentship; (parent: $p, child: $c); $p has identifier $pname; $c has identifier $cname;``` but lots of duplicates?! -->

{% include note.html content="In queries, Graql variables start with a `$`, which represent wildcards, and are returned as results in `match` queries. A variable name can contain alphanumeric characters, dashes and underscores." %}

### Extending the Graph

Besides make `match` queries, it is also possible to [`insert`](../graql/insert-queries.html) and [`delete`](../graql/delete-queries.html) items in the graph through the Graql shell.

```graql
insert $gormenghast isa person has firstname "Titus" has identifier "Titus Groan" has surname "Groan" has gender "male";
commit;
```

{% include note.html content="<b>Don't forget to `commit`!</b> <br /> Nothing you have entered into the Graql shell has yet been committed to the graph, nor has it been validated. To save any changes you make to a graph, you need to type `commit` in the shell. It is a good habit to get into regularly committing what you have entered." %}

To find your inserted `person`:

```graql
match $x isa person has identifier "Titus Groan"; 
```

To delete the `person` again:

```graql
match $x isa person has identifier "Titus Groan"; delete $x;
```

## Using the Grakn Visualiser

The [Grakn visualiser](../grakn-dashboard/visualiser.html) provides a graphical tool to inspect and query your graph data. You can open the visualiser by navigating to [localhost:4567](http://localhost:4567) in your web browser. The visualiser allows you to make queries or simply browse the types within the graph. The screenshot below shows a basic query (`match $x isa person;`) typed into the form at the top of the main pane, and visualised by pressing "Submit":

![Person query](/images/match-$x-isa-person.png)

The help tab on the main pane shows a set of key combinations that you can use to further drill into the data. You can zoom the display in and out, and move the nodes around for better visibility. Please see our [Grakn visualiser](../grakn-dashboard/visualiser.html) documentation for further details.


## Using Inference

The use of GRAKN.AI to infer new information about a dataset lies at its core. We have a [detailed example of using the Grakn reasoner](../examples/graql-reasoning.html) to infer information about the genealogy dataset. An additional discussion on the same topic can be found in our ["Family Matters" blog post](https://blog.grakn.ai/family-matters-1bb639396a24#.525ozq2zy).

## Using Analytics

{% include note.html content="The text in this section is a placeholder, and will be replaced with more detail in early 2017." %}

Use of Grakn Analytics is covered in [Analytics](../graql-analytics/analytics-overview.html).


## Data Migration

Migrating data in formats such as CSV, SQL, OWL and JSON into Grakn is a key use case. We have used a simple example here, which loaded *basic-genealogy.gql* data directly into a graph. However, that file is based on CSV data that can be migrated into Grakn to provide a more complex graph. The [CSV migration example](../examples/CSV-migration.html) explains the steps in further detail. There are a number of other formats besides CSV that are supported, and more information can be found in the [migration documentation](../migration/migration-overview.html).

## Where Next?

This page was a very high-level overview of some of the key use cases for Grakn, and has hardly touched the surface or gone into detail. The rest of our developer documentation and examples are more in-depth and should answer any questions that you may have, but if you need extra information, please [get in touch](https://grakn.ai/community.html).

A good place to start is to explore our additional [example code](../examples/examples-overview.html) and the documentation for [Graql](../graql/graql-overview.html), [migration](../migration/migration-overview.html), [analytics](../graql-analytics/analytics-overview.html) and the [API reference](https://grakn.ai/javadocs.html). 

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/17" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
