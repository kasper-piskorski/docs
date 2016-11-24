---
title: Grakn Analytics
keywords:
last_updated: November 24, 2016
tags:
summary: "This page provides an overview of the Grakn analytics capabilities."
sidebar: documentation_sidebar
permalink: /documentation/grakn-analytics/analytics-overview.html
folder: documentation
comment_issue_id:
---

The distributed Grakn knowledge graph presents two opportunities to obtain novel insight across your data. The first
involves intelligently aggregating large amounts of information. For example, finding out the mean and standard
deviation of vehicles owned by companies (not individuals), no matter how big your database. Graql allows you to specify
what you want instead of how to get it, and analytics allows you to do it at scale.

The second opportunity is a result of the data forming a graph. The structure of the graph itself contains valuable
information about the importance of entities and also the communities they form. An example of this is our own website
moogi that uses only the structure of the graph to rank the results. This is achieved by computing the number of
relationships that certain entities take part in, and using this as a measure of how popular they are.

{% include note.html content="Under the hood we use implementations of the Pregel distributed graph computing
framework and map reduce when a graph is not important. This way we can implement algorithms that will scale horizontally.
" %}

Grakn analytics functionality is accessed via the compute query in the graql language. In order to fully understand the
syntax a slightly more in depth understanding of the graph is needed.

## The Knowledge Graph According to Analytics

Analytics only "sees" the instances of types, but is aware of the ontology. Therefore, if your graph has a type `car`
then the instances of this: `Mike's car`, `Dave's car` and `Alice's car` can be counted using analytics.
```
compute count in car;
```
Often you are not interested in the whole knowledge graph when performing calculations and it is possible in Graql to
specify a subgraph (a subset of instances to work on). In the previous example we counted cars, but imagine the
knowledge graph also contained trucks, trains, people and the relationships between them. These have effectively been
excluded from the calculation by specifying a subgraph using the `in` keyword.

Consider the graph below that shows a very simple graph including types, instances and some relationships. Analytics
will consider every node in the graph apart from `person`, `writes` and `comment` which are types. If we were to
compute the count on this graph without specifying any subgraph `compute count;` the number 6 would be returned.
Analytics has counted all of the instances of the types, which are specific comments, people and the nodes representing
the relationship between a comment and its writer.

![A simple graph.](/images/analytics_sub_Graph.png)

A subgraph is defined in analytics by using the types. For example we could specify a subgraph that includes only
`person` and `writes` but not `comment`. The graph that analytics will now operate on can be seen below. Why would
this matter? Well the first purpose is for efficiency - we do not have to count the things we are not interested in.
The second reason is related to how specific algorithms operate.

![A simple graph.](/images/analytics_another_sub_Graph.png)

The algorithm for computing the degree is one example. If we execute the following query
```
compute degrees in person, writes;
```
the number of arrows attached to each node is returned. In the subgraph example above this would be 1 for Dimitru and
0 for Antonio. Remember we do not see types, and we also do not see the arrows indicating type, only the arrows
labelled with roles. This graph also happens to include relationships between people and between messages, but we have
effortlessly ignored these and found out how many messages a person has written for any size of graph.

{% include note.html content="The degree is the simplest measure of the importance (centrality) of a node in a graph.
Graql allows us to very flexibly define the subgraph in which we want to compute the degree, and therefore determine
importance according to various structures in the graph.
" %}

## What Can I do With Analytics?

The functionality breaks down into two main tasks: computing statistics related to numeric resources and interrogating
the structure of the graph.

### Statistics

Currently you can compute the min, max, mean, median, standard deviation and sum of resources attached to entities. This
can also be achieved on a subgraph allowing you to specify queries like:
```
compute mean of price in car, truck;
```
to find the mean price of all the cars and trucks in the graph.

### Graph Structure

At the moment we have a simple algorithm for determining a centrality measure (degree), paths between nodes (shortest
path) and communities (connected components).
