---
title: Quickstart Tutorial
keywords: setup, getting started
last_updated: December 2016
tags: [getting-started, graql]
summary: "This document will work through a simple example using the Graql shell to show how to get started with GRAKN.AI."
sidebar: documentation_sidebar
permalink: /documentation/the-basics/quickstart-tutorial.html
folder: documentation
comment_issue_id: 17
---

## Introduction
If you have not yet set up GRAKN.AI, please [download the latest version](../resources/downloads.html), then start the Grakn engine and the Graql shell.

```bash
./bin/grakn.sh start
./bin/graql.sh
```

If you're unsure of these steps, don't worry. Please see the [Setup guide](../get-started/setup-guide.html).

This example is loosely based on the genealogy-graph example that we use throughout our documentation. You can find the full example, including data and ontology, in the [sample-datasets repo on Github](https://github.com/graknlabs/sample-datasets/tree/master/genealogy-graph). However, we are not going to load the complete data and ontology here: this is a basic "quick start" example to illustrate addition of an ontology and some data to Grakn, using the Graql shell. 

## Using the Graql Shell

### Adding an Ontology

We are going to add some basic ontology concepts: entities, relations, roles and resources. Our example is very simple: a `person` entity with a resource named `identifier`, which is a string to hold the person's name. 

Copy and paste the following into the Graql shell:

```graql
insert person sub entity, has-resource identifier; identifier sub resource, datatype string;
```

Now we will add a relation: a `marriage` which relates two `person` entities, one playing the role `spouse1` and the other playing the role `spouse2`.  Only a `person` can take a role in the `marriage` (not a `car` or `dog` for example), so we also need to specify that to Grakn. Copy and paste each of the following lines into the Graql shell:

```graql
insert spouse1 sub role; spouse2 sub role; person plays-role spouse1; person plays-role spouse2;
insert marriage sub relation, has-role spouse1, has-role spouse2, has-resource date; date sub resource datatype string;
```

### Adding Data

Now we will add some sample data to the graph, to correspond to the ontology we entered above. First, add four instances of `person`:

```graql
insert isa person, has identifier "Andrew Smith";
insert isa person, has identifier "Catherine Shaw";
insert isa person, has identifier "Paula Carter";
insert isa person, has identifier "Scott Jones";
```

Now we will add an instance of `marriage` between two of the `person` instances:

```graql
match $s1 has identifier "Andrew Smith"; $s2 has identifier "Catherine Shaw"; insert (spouse1: $s1, spouse2: $s2) isa marriage has date "01-01-1980";
commit
```

The `match .... insert ...` syntax will perform the `insert` query for every result of the `match` query.


{% include note.html content="<b>Don't forget to `commit`!</b> <br /> Nothing you have entered into the Graql shell has yet been committed to the graph, nor has it been validated. To save any changes you make to a graph, you need to type `commit` in the shell. It is a good habit to get into regularly committing what you have entered." %}

### Querying Data
Check that the data has loaded, by performing a pair of queries:

Find all people in the graph:

```graql
match $p isa person, has identifier $i;
```

Find all people who are married:

```graql
match (spouse1: $x, spouse2: $y) isa marriage, has date $d; $x has identifier $xi; $y has identifier $yi;  
```

{% include note.html content="In queries, Graql variables start with a `$`, which represent wildcards, and are returned as results in `match` queries. A variable name can contain alphanumeric characters, dashes and underscores." %}

### Loading the Ontology and Data from File

Above, was a quick overview to show the basics of making Graql queries on some basic data within the Graql shell. It is much more common to load the ontology and data directly from *.gql* files into a graph instead of typing it all into the shell. You can find a good example of this in the [Visualising a Graph](./visualiser.html) documentation, which takes our full genealogy data, loads it into a graph and explores it using the Grakn visualiser. Please see the [examples section](../examples/examples.html) of this documentation for additional examples.

We shall now move on to look briefly at some of the other use cases that are typical for developers working with GRAKN.AI.

## Data Migration

Migrating data in formats such as CSV, SQL, OWL and JSON into Grakn is a key use case. We discuss this in [Migration](../migration/migration-overview.html) and have a number of examples, such as an example of [migrating genealogy data from CSV](../examples/CSV-migration.html). 

## Using Inference

The use of GRAKN.AI to infer new information about a dataset lies at its core. We have a [detailed example of using the Grakn reasoner](../examples/grakn-reasoner.html) to infer information about the genealogy dataset. An additional discussion on the same topic can be found in our ["Family Matters" blog post](https://blog.grakn.ai/family-matters-1bb639396a24#.525ozq2zy).

## Using Analytics

{% include note.html content="The text in this section is a placeholder, and will be replaced with more detail in early 2017." %}

Use of Grakn Analytics is covered in [Analytics](../graql-analytics/analytics-overview.html).


## Where Next?

This page was a very high-level overview of some of the key use cases for Grakn, and has hardly touched the surface or gone into detail. The rest of our developer documentation and examples are more in-depth and should answer any questions that you may have, but if you need extra information, please [get in touch](https://grakn.ai/community.html).

A good place to start is to explore our additional [example code](../examples/examples-overview.html) and the documentation for [Graql](../graql/graql-overview.html), [migration](../migration/migration-overview.html), [analytics](../graql-analytics/analytics-overview.html) and the [API reference](https://grakn.ai/javadocs.html). 

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/17" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
