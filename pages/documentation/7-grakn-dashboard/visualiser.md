---
title: Visualising a Grakn Graph
keywords: setup, getting started
last_updated: August 10, 2016
tags: [getting-started, graql]
summary: "How to use the Grakn Visualiser."
sidebar: documentation_sidebar
permalink: /documentation/grakn-dashboard/visualiser.html
folder: documentation
comment_issue_id: 17
---

## Introduction
The Grakn visualiser provides a graphical tool to inspect and query your graph data. This article shows how to get it up and running on a basic example and introduces the visualiser's key features.

## Loading and Visualising a Graph
If you have not yet set up the Grakn environment, please see the [Setup guide](../get-started/setup-guide.html).

You can find the *basic-genealogy.gql* example that we will work with in the *examples* directory of the Grakn distribution zip. You can also find this file on [Github](https://github.com/graknlabs/grakn/blob/master/grakn-dist/src/examples/basic-genealogy.gql). 

The first step is to load the ontology and data into Grakn. You need to use your terminal to do this, as the visualiser is a read-only interface to a graph. From the terminal, start Grakn, and load the file as follows:

```bash
<relative-path-to-Grakn>/bin/grakn.sh start
<relative-path-to-Grakn>/bin/graql.sh -f ./examples/basic-genealogy.gql -k "family"
```

{% include note.html content="To illustrate the use of different keyspaces, will we use a keyspace called `family` in this example, but you can simply use the default keyspace if you prefer, if it is not already in use, by omitting the -k argument." %}

You can test in the Graql shell that all has loaded correctly. For example:

```bash
<relative-path-to-Grakn>/bin/graql.sh -k family
match $p isa person, has identifier $i;
```

Now open the visualiser by browsing to [localhost:4567](http://localhost:4567). 

To visualise your graph, go to the keyspace selector on the top right and select the appropriate keyspace, e.g. `family`.

The main pane of your graph will be empty at this point. Click the Types dropdown in the top menu, then Entities and `person`. 
The query for your selection will be displayed in the form with a default limit of 100 results applied. In this case:

```graql
match $x isa person; offset 0; limit 100;
```

If you click and hold on any of the entities - a pop-up will open to allow you to select the labels shown on each node in the graph. In the screenshot below, we have selected to show the identifiers of each person.

![Person query](/images/match-$x-isa-person.png)

## Working With The Visualiser

There are a number of horizontal tabs on the left hand side of the screen, described as follows.

## Pages

### Graph
This is the main section of the visualiser that you will use to explore the graph. You can submit queries using the form, then press ">" to visualise the graph. For example:

```graql
match $x isa person, has firstname "John"; 
```

You can zoom the display in and out, and move the nodes around for better visibility. 

![John query](/images/john-query.png)

To clear the graph, press Shift + the "Clear" button.

We have already shown an example of how to examine `person` entities using the entity selector. As another example, select "Types", followed by "Relations" and filter on `marriage` relations. The query will be shown in the query section at the top of the main pane, as previously, and the visualiser displays all the `marriage` relations in the graph. 

![Marriages query](/images/marriages.png)

There are also 3 query settings, which can be changed using the 'cog' button:

* Activate inference - activates inference, per query.
* Materialise inference - persists the inference into the graph, per query.
* Materialise All: activate and persist all inference across the graph.


### Console
You can use this console to make queries instead of running a Graql shell in your terminal. You can run `match` and `compute` queries, but because the visualiser is read-only, you cannot make insertions.

### Config
Shows a view on the Grakn configuration file. 

### Documentation
This opens a separate tab in your browser and points it to the Grakn documentation portal. It may be how you ended up on this page!


## Where Next?

Now you have started getting to grips with Grakn, please explore our additional [example code](../examples/examples-overview.html) and documentation. We are always adding more and welcome ideas and improvement suggestions. Please [get in touch](https://grakn.ai/community.html)!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/17" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.