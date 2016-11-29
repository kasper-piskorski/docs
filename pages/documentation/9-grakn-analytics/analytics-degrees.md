---
title: Degrees
keywords:
last_updated: November 24, 2016
tags:
summary: "This page introduces the computation of degrees in a graph."
sidebar: documentation_sidebar
permalink: /documentation/grakn-analytics/analytics-degrees.html
folder: documentation
comment_issue_id:
---

What is the most important instance in my graph? Given no more context than this the degree can provide an answer of
sorts. The degree of an instance gives the number of other instances directly connected to it. For an entity, such as
a person, this means the number of relations they are connected to, for example instances of a friendship. In this case
the person with the highest degree, number of friends, is arguably the most important.

In Grakn your graph will be much more expressive than just containing people and friendships, and computing the degree
alone may not be very informative. If the graph also included messages being sent, we might not want to include these
when deciding if a person was important or not. The specific degree we are interested in can be easily computed with
this Graql query:

```
compute degrees in person, knows;
```