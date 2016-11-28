---
title: Connected Components
keywords:
last_updated: November 24, 2016
tags:
summary: "This page introduces the connected components algorithm and explains how to use it."
sidebar: documentation_sidebar
permalink: /documentation/grakn-analytics/analytics-connected-components.html
folder: documentation
comment_issue_id:
---

The connected components algorithm can be used to find clusters of instances in the graph that are connected. The
 algorithm finds all instances (relations, resources and entities) that are connected via relations in the graph and
 gives each set a unique label. In the graph below you can see three connected components that correspond to groups of
 friends. In this graph three unique labels will be created one corresponding to each of the sets of connected instances.

 ![Three connected components representing groups of friends.](/images/analytics_conn_comp.png)

