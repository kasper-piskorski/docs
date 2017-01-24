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


<!--

Show how to use the types to visualise the ontology
Click and hold on a node you can change the properties that you see
Click to see the drawer on the right

### Visualiser Colour Scheme

The colours used in the visualiser are as follows:

| Colour | Description                                   |
| ----------- | --------------------------------------------- |
| Pink     | Ontology meta-types                        |
| Blue      | Data entity                         |
| Green     | Ontology relation                       |
| Orange     | Label                        |
| Yellow     |                         |
|      |                         |

-->

## Introduction
The Grakn visualiser provides a graphical tool to inspect and query your graph data. This article shows how to get it up and running on a basic example and introduces the visualiser's key features.

## Loading and Visualising a Graph
If you have not yet set up the Grakn environment, please see the [Setup guide](../get-started/setup-guide.html).

You can find the *basic-genealogy.gql* example that we will work with in the *examples* directory of the Grakn distribution zip. You can also find this file on [Github](). 

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

The visualiser has a number of horizontal navigation tabs on its left hand side, below the version number. We will go through each of them below, but to visualise your graph at this point in the example, you need to go to the keyspace tab and select the appropriate keyspace, e.g. `family`.

The main pane of your graph will be empty at this point. Click the Types button in the top right, then the Visualise button at the top right hand side of the newly opened drawer. The genealogy-graph ontology will be displayed.

![Genealogy Ontology](/images/genealogy-ontology.png)

## Working With The Visualiser

There are a number of horizontal tabs on the left hand side of the screen, described as follows.

## Pages

### Graph
This is the main section of the visualiser that you will use to explore the graph. You can use the query section at the top of the main pane, then press "Submit" to visualise it. For example:

```graql
match $x isa person;
```

![Person query](/images/match-$x-isa-person.png)

The help tab on the main pane shows a set of key combinations that you can use to further drill into the data. You can zoom the display in and out, and move the nodes around for better visibility.

Alternatively, you can use the "Types" drawer to filter on specific types. For example, clear the graph using the "Clear" button, then select "Types", followed by "Entities" and filter on `picture` resources. The equivalent query will be shown in the query section at the top of the main pane, and the visualiser displays all the `picture` resources in the graph. These resources are strings that are URLs, and you can see the information the graph stores for each by single clicking one of them, at which point a drawer slides out on the right hand side, as shown: 

![Picture query](/images/match-$x-isa-picture.png)

### Console
You can use this console to make queries instead of running a Graql shell in your terminal. You can run `match` and `compute` queries, but because the visualiser is read-only, you cannot make insertions.

### Config
Shows a view on the Grakn configuration file. There are also 3 inference settings:

* Activate Inference - activates inference, per query.
* Materialise Inference - persists the inference into the graph, per query.
* Materialisation: activate and persist all inference across the graph.

### Documentation
This opens a separate tab in your browser and points it to the Grakn documentation portal. It may be how you ended up on this page!

## Actions

### Keyspaces
The keyspaces that are currently in use by Grakn.


## Where Next?

Now you have started getting to grips with Grakn, please explore our additional [example code](../examples/examples-overview.html) and documentation. We are always adding more and welcome ideas and improvement suggestions. Please [get in touch](https://grakn.ai/community.html)!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/17" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.