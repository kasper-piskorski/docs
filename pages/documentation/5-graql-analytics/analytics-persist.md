---
title: Persisting to the Graph
last_updated: November 24, 2016
tags: [analytics]
summary: ""
sidebar: documentation_sidebar
permalink: /documentation/graql-analytics/analytics-persist.html
folder: documentation
comment_issue_id: 71
---

Once you have spent the time necessary to compute something like the degree of entities in your graph it is only natural that you may want to use these values again. This is where the persist behaviour comes in. If the result of your compute query is a map from instances to values, these can be persisted in the graph at the end of the calculation. The persistence is performed in parallel, as a bulk loading task, and should scale as well as the other algorithms.
{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/71" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of this page.
