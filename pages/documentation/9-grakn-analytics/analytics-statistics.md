---
title: Statistics
last_updated: November 24, 2016
tags: [analytics]
summary: "This page introduces the statistics functionality of analytics."
sidebar: documentation_sidebar
permalink: /documentation/graql-analytics/analytics-statistics.html
folder: documentation
comment_issue_id: 71
---

Computing simple statistics such as the mean and standard deviations of datasets is an easy task when considering a few
isolated instances, but what happens when your graph becomes so large that it is distributed across many machines? What
if the values you are considering are related to many different types of things? For example cars, trucks, motorbikes,
or any other vehicles.

Graql statistics provides the solution using the descriptive power of graql combined with its analytics capabilities. It
is easy to describe what you are interested in computing and Graql will intelligently figure out how to do it.

```
compute mean of price in vehicles;
```

The above query will determine that cars, trucks and motorbikes are types of vehicle using the ontology, and then
execute a distributed computation to determine the mean price of all the vehicles in the graph. The available algorithms
are: min, max, sum, mean, standard deviation, and median.

<!-- JCS Comments: Please can you give a small example of each? I think the above is a bit promotional in tone, so I will probably revise it, so we keep this page as a tutorial guide - we can use this kind of description in a blog post though! -->

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/71" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of this page.
