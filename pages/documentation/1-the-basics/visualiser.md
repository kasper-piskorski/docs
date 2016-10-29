---
title: Visualising a Grakn Graph
keywords: setup, getting started
last_updated: August 10, 2016
tags: [getting-started, graql]
summary: "How to use the Grakn Visualiser."
sidebar: documentation_sidebar
permalink: /documentation/the-basics/visualiser.html
folder: documentation
comment_issue_id: 52
---

## Introduction
The Grakn visualiser provides a graphical tool to inspect and query your graph data. This article shows how to get it up and running on a basic example and introduces the visualiser's key features.

## Loading and Visualising a Graph
If you have not yet set up the Grakn environment, please see the [Setup guide](../get-started/setup-guide.html).
You can find the ```pokemon.gql``` example we will work with in the examples directory that is included in the Grakn download zip, or in our [Grakn repo](https://github.com/graknlabs/grakn/tree/master/mindmaps-dist/src/examples).

The first step is to load the Pokemon graph from the example file into the default keyspace. You need to use your terminal to do this, as the visualiser is a read-only interface to a graph.

```bash
bin/graql.sh -f examples/pokemon.gql
```

While in the terminal, you can test in the Graql shell that all is well. For example:

```bash
bin/graql.sh
>>>match $x isa pokemon; limit 5;
$x id "Venusaur" isa pokemon; 
$x id "Raticate" isa pokemon; 
$x id "Nidorina" isa pokemon; 
$x id "Victreebel" isa pokemon; 
$x id "Haunter" isa pokemon; 
```

Now to open the visualiser by browsing to [localhost:4567](http://localhost:4567). 

The visualiser has 4 horizontal navigation tabs on its left hand side, below the version number. 

### Status
The Status pane lists various configuration items and their current settings.

### Graph
This is the main section of the visualiser that you will use to explore the graph. Type in a query to receive a graphical view on the data. For example, `match $x isa pokemon; limit 10;`

![Pokemon query](/images/pokemon-query-visualiser.jpg)

The help pane shows a set of key combinations that you can use to further drill into the data, and the "Show Types" dropdown menu allows you to filter on specific types.

### Console
You can use this console to make queries instead of running a Graql shell in your terminal. 

### Documentation
This opens a separate tab in your browser on the Grakn documentation portal.

## Where Next?

Now you have started getting to grips with Grakn, please explore our additional [example code](../examples/examples.html) and documentation. We are always adding more and welcome ideas and improvement suggestions. Please [get in touch](https://grakn.ai/community.html)!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/52" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.