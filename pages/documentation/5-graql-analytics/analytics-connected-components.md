---
title: Connected Components
last_updated: November 24, 2016
tags: [analytics]
summary: "This page introduces the connected components algorithm and explains how to use it."
sidebar: documentation_sidebar
permalink: /documentation/graql-analytics/analytics-connected-components.html
folder: documentation
comment_issue_id: 71
---

The connected components algorithm can be used to find clusters of instances in the graph that are connected. The
 algorithm finds all instances (relations, resources and entities) that are connected via relations in the graph and
 gives each set a unique label. In the graph below you can see three connected components that correspond to groups of
 friends. In this graph three unique labels will be created one corresponding to each of the sets of connected instances.

 ![Three connected components representing groups of friends.](/images/analytics_conn_comp.png)

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/71" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of this page.


