---
title: Quickstart Tutorial
keywords: setup, getting started
last_updated: December 2016
tags: [getting-started, graql]
summary: "This document will work through a simple example using the Graql shell to show how to get started with GRAKN.AI."
sidebar: documentation_sidebar
permalink: /documentation/the-basics/quickstart-tutorial.html
folder: documentation
comment_issue_id: 18
---

<!--Will eventually cover the following:
insert ontology and data, and test out a basic query plus visualiser it
migrate data using graql migrator,
query data with no inference
insert rules,
query data with inference
analyse data using graql analytics
delete data
-->


## Introduction
If you have not yet set up GRAKN.AI, please [download the latest version](../resources/downloads.html), then start the Grakn engine and the Graql shell.

```bash
./bin/grakn.sh start
./bin/graql.sh
```

If you're unsure of these steps, don't worry. Please see the [Setup guide](../get-started/setup-guide.html).

This example is loosely based on the genealogy example that we use throughout our documentation. You can find the full example, including data and ontology, in the [sample-datasets repo on Github](https://github.com/graknlabs/sample-datasets/tree/master/genealogy-graph). However, we are not going to load the data and ontology at this point, but initially we will simply show how to work with Graql to set up an ontology and add some data to a graph. 

## Ontology

We are going to add ontology concepts for some people, their notable events (when they are born, get married, have children, die) and relationships (children, parents, spouses). 

Let's add our first concepts: an `entity-type` named `person` that has a `resource-type` named `identifier`, which is a string to hold the person's name. 
Copy and paste the following into the Graql shell:

```graql
insert person sub entity, has-resource identifier; identifier sub resource, datatype string;
```

Now we will add a `relation-type`: a `marriage` which relates two `person` entity-types, one playing `role-type` of `spouse1` and the other playing the `role-type` of `spouse2`.  Only a `person` can take a role in the `marriage` (not a `car` or `dog` for example), so we also need to specify that to Grakn. Copy and paste each of the following lines into the Graql shell:

```graql
insert spouse1 sub role; spouse2 sub role; person plays-role spouse1; person plays-role spouse2;
insert marriage sub relation, has-role spouse1, has-role spouse2, has-resource date; date sub resource datatype string;
```

## Data

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

Check that the data has loaded, by performing a pair of queries:

```graql
# Find all people in the graph
match $p isa person, has identifier $i;

# Find all people who are married
match (spouse1: $x, spouse2: $y) isa marriage, has date $d; $x has identifier $xi; $y has identifier $yi;  
```

{% include note.html content="In queries, Graql variables start with a `$`, which represent wildcards, and are returned as results in `match` queries. A variable name can contain alphanumeric characters, dashes and underscores." %}


## Where Next?

Now you have started getting to grips with Grakn, please explore our additional [example code](../examples/examples.html) and documentation. [Modern](../examples/modern.html) is an additional example of adding an ontology and data to a graph and making test queries, which is useful for getting to grips with Graql syntax.

We are always adding more and welcome ideas and improvement suggestions. Please [get in touch](https://grakn.ai/community.html)!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/18" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
