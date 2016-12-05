---
title: Graql Analytics
last_updated: December 5th, 2016
tags: [analytics]
summary: "This page provides an overview of the Graql analytics capabilities."
sidebar: documentation_sidebar
permalink: /documentation/graql-analytics/analytics-overview.html
folder: documentation
comment_issue_id: 71
---

The distributed Grakn knowledge graph presents two different ways to obtain insight on a large dataset:   
 
 *   It intelligently aggregates large amounts of information. Graql allows you to specify what you want, instead of how to get it, and analytics allows you to do it at scale. For example, finding out the mean number and standard deviation of vehicles owned by companies (not individuals), no matter how big the dataset. 
 *  The structure of the graph contains valuable information about the importance of entities and also the communities they form. This is achieved by computing the number of relationships that certain entities take part in, and using this as a measure of how popular they are. An example of this can be seen on the [Moogi website](https://moogi.co), which uses only the structure of the graph to rank the results. 

<!-- JCS Comments: Please can you clarify "graph is not important" as I don't understand what you mean, and also provide a suitable link to more about Pregel and map reduce  ? -->

{% include note.html content="Under the hood we use implementations of the Pregel distributed graph computing
framework and map reduce when a graph is not important. This way we can implement algorithms that will scale horizontally." %}

## What Can I do With Analytics?

The functionality breaks down into two main tasks: 

*  computing statistics related to numeric resources 
*  interrogating the structure of the graph.

### Statistics

Currently you can compute the `min`, `max`, `mean`, `median`, `sd` (standard deviation) and `sum` of resources attached to entities. This
can also be achieved on a subgraph, which is a subset of the types in your dataset. For example, you can specify queries to find the mean price of cars and trucks in a graph:   

```
compute mean of price in car, truck;
```

We cover this topic more in our documentation page on [statistics](./analytics-statistics.html).

### Graph Structure

At the moment we have a simple algorithm for determining 

* [centrality measure (degree)](./analytics-degrees.html)
* [paths between nodes (shortest path)](analytics-shortest-path.html) 
* [clusters or communities (connected components)](./analytics-connected-components.html). 

## The Knowledge Graph According to Analytics

Graql analytics functionality is accessed via the `compute` query in the Graql language. In order to fully understand the
syntax, an in-depth understanding of the graph is needed, so we will dive into some details here.

Analytics only "sees" the instances of types, but is aware of the ontology. Therefore, if your graph has a type `car`
then the instances of this: `Mike's car`, `Dave's car` and `Alice's car` can be counted using analytics.  Often you are not interested in the whole knowledge graph when performing calculations, and it is possible to specify a subgraph (a subset of your data to work on) to Graql. For example, a knowledge graph may contain cars, trucks, trains, people and the relationships between them, but these can be excluded by specifying a subgraph using the `in` keyword.  To count just cars: 

```
compute count in car;
```

Consider the simple graph below that includes types, instances and some relationships. Analytics will consider every instance in the graph, so will not consider the types `person`, `writes` and `comment`, coloured in blue). To compute the count on this graph without specifying any subgraph, we call the following, which returns the number 6:   

```
compute count;
``` 

Analytics has counted all of the instances of the types, which are specific comments, people and the nodes representing
the relationship between a comment and its writer.

![A simple graph.](/images/analytics_sub_Graph.png)

A subgraph is defined in analytics by using the types. For example, we could specify a subgraph that includes only
`person` and `writes` but not `comment`. 

<!-- JCS Comments: How do we do that? -->

![A simple graph.](/images/analytics_another_sub_Graph.png)

The graph that analytics will now operate on can be seen above. 

We may specify a subgraph for efficiency (so we do not have to count the things we are not interested in) but also because of how specific algorithms operate. The algorithm for computing the degree is one example. If we execute the following query, the number of arrows attached to each node is returned:   

```
compute degrees in person, writes;
```

In the subgraph example above this would be 2 for Dimitru and 0 for Antonio because we do not count the arrows indicating type, only arrows labelled with roles. 

This graph also happens to include relationships between people and between messages, but we have effortlessly ignored these and found out how many messages a person has written for any size of graph.

<!-- JCS Comments: Sorry - this doesn't make sense to me. Please could you rephrase for the hard of thinking, or ping me to explain, so I can reword it when I understand? -->

{% include note.html content="The degree is the simplest measure of the importance (centrality) of a node in a graph.
Graql if very flexible and allows us to define the subgraph in which we want to compute the degree, and therefore determine
importance according to various structures in the graph.
" %}

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/71" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of this page.
