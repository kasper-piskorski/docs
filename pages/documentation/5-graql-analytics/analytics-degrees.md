---
title: Degrees
last_updated: December 12th, 2016
tags: [analytics]
summary: "This page introduces the computation of degrees in a graph."
sidebar: documentation_sidebar
permalink: /documentation/graql-analytics/analytics-degrees.html
folder: documentation
comment_issue_id: 71
---

What is the most important instance in a graph? Given no more context than this, `degrees` can provide an answer of
sorts. The degree of an instance gives the number of other instances directly connected to the instance. For an entity, such as
a `person`, this means the number of connections, for example instances of a `friendship` relationship. The `person` with the highest degree, or the greatest number of friends, is arguably the most "important".

### Degrees

The `degrees` algorithm computes how many edges there are attached to instances in the graph. A map is returned
that displays an instance ID and its degree. If we call:

```
compute degrees;
```

on the graph below we expect the degrees of the people to be: 

* **Evangelos**: 1; Evangelos has one edge; labelled "acquaintance1"
* **Alim**: 4: Alim has 4 edges; 2 labelled "writer", and 2 labelled "acquaintance2"
* **Eric**: 2: Eric has 2 edges; 1 edge labelled "writer" and 1 labelled "acquaintance1"

We do not count the edges labelled isa, because they are connected to the types and are not considered by analytics.
Don't forget that the relations and comments will also have degrees.

![A simple social network.](/images/analytics_degree_full.png)

### Degrees Within a Subgraph

Consider that in this graph, people with more friends are more important. We can use the subgraph functionality to restrict the graph to only see people and who they are friends with. Once the graph has been restricted we can determine the number of friends by computing the degree:

```
compute degrees in person, knows;
```

The result will now be: 

* **Evangelos**: 1
* **Alim**: 2
* **Eric**: 1

Next we might be interested in who writes the most messages. In a similar way we can restrict the calculation to a different subgraph to acquire the desired information.

```
compute degrees in person, writes;
```

The answer is now different: 

* **Evangelos**: 0
* **Alim**: 2
* **Eric**: 1

### Persisting the Degrees

Sometimes we want the information above to be made available in the live system. For this reason we have introduced `persist`, which is [documented further on a separate page](./analytics-persist.html). 

To persist the degrees to the graph we call:

```
compute degrees in person, knows; persist;
```

This will result in the graph shown below, with degrees that can now be queried using Graql. 

![A simple social network.](/images/analytics_persist_degrees.png)

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/71" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of this page.
