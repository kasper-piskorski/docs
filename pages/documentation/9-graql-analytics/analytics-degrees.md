---
title: Degrees
last_updated: November 24, 2016
tags: [analytics]
summary: "This page introduces the computation of degrees in a graph."
sidebar: documentation_sidebar
permalink: /documentation/graql-analytics/analytics-degrees.html
folder: documentation
comment_issue_id: 71
---

What is the most important instance in my graph? Given no more context than this, the degree can provide an answer of
sorts. The degree of an instance gives the number of other instances directly connected to it. For an entity, such as
a person, this means the number of relations they are connected to, for example instances of a friendship. In this case
the person with the highest degree, number of friends, is arguably the most important.

## Available Algorithms

| Algorithm | Description                                   |
| ----------- | --------------------------------------------- |
| [`degrees`](#degrees)       | Compute the degree of the instances.                           |
| [`degreesAndPersist`](#degrees-and-persist)    | Compute the degree of the instances and persist this in the graph. |

### Degrees

The `degrees` algorithm computes how many edges there are attached to a given instances in the graph. A map is returned
that displays an instance ID and its degree.

```
compute degrees in person;
```

### Degrees and Persist

This algorithm starts by calling [`degrees`](#degrees). Instead of returning the map of results it persists each degree
to its respective instance in the graph using a resource.

```
compute degreesAndPersist in person;
```


{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/71" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of this page.
