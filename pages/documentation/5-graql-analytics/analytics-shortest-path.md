---
title: Shortest Path
last_updated: November 24, 2016
tags: [analytics]
summary: ""
sidebar: documentation_sidebar
permalink: /documentation/graql-analytics/analytics-shortest-path.html
folder: documentation
comment_issue_id: 71
---

How are two instances in the graph related?
When starting a task you don't always know in advance what you are looking for.
Finding the shortest path between two instances in a graph can be a great way to explore connections because you do not need to provide any guidance.
In the graph below I have displayed all of the people and then searched for relationships joining two of them using:

```
compute path from "41001152" to "20496";
```

You can see that the two people selected are friends of friends.
The path query uses a scalable shortest path algorithm to determine the smallest number of relations required to get from once concept to the other.

![Shortest path between people.](/images/analytics_path_knows.png)

### Subgraph

If you are looking for more specific connections you can of course use the [subgraph](./analytics_overview.html) functionality.
In the following query only the paths through comments are investigated and the resulting graph is shown below.
We have excluded friendships in this subgraph and now the shortest path is longer than before.

```
compute path from "20496" to "36960" in person, comment, writes, reply;

```

![Shortest path between people.](/images/analytics_path_comments.png)

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/71" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of this page.
